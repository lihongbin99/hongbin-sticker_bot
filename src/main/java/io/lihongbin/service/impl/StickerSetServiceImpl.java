package io.lihongbin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import io.lihongbin.config.FileConfig;
import io.lihongbin.entity.Sticker;
import io.lihongbin.entity.StickerSet;
import io.lihongbin.mapper.StickerSetMapper;
import io.lihongbin.service.StickerService;
import io.lihongbin.service.StickerSetService;
import io.lihongbin.utils.TelegramUtils;
import io.lihongbin.utils.UpdateUtil;
import io.lihongbin.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
@Slf4j
@Service
public class StickerSetServiceImpl extends ServiceImpl<StickerSetMapper, StickerSet> implements StickerSetService {

    private StickerSetMapper stickerSetMapper;

    private StickerService stickerService;

    private FileConfig fileConfig;

    public StickerSetServiceImpl(StickerSetMapper stickerSetMapper, StickerService stickerService, FileConfig fileConfig) {
        this.stickerSetMapper = stickerSetMapper;
        this.stickerService = stickerService;
        this.fileConfig = fileConfig;
    }

    @Override
    public StickerSet getStickerSetBySetName(String setName) {
        LambdaQueryWrapper<StickerSet> queryWrapper = new QueryWrapper<StickerSet>().lambda();
        queryWrapper.eq(StickerSet::getName, setName);
        return stickerSetMapper.selectOne(queryWrapper);
    }

    @Override
    public Sticker addStickerSet(com.pengrad.telegrambot.model.Sticker sticker, final TelegramBot BOT) throws IOException {
        Sticker mysqlSticker = null;
        List<Sticker> stickerList = new ArrayList<>();
        // 获取完整贴纸集
        com.pengrad.telegrambot.model.StickerSet stickerSet = TelegramUtils.getStickerSet(sticker.setName(), BOT);

        // 000001
        String directoryName = fileConfig.getNewStickerDirectory();
        // C:\Users\Administrator\Desktop\telegram\sticker
        String stickerDirectory = fileConfig.directoryNameToCanonicalPath(directoryName);
        // C:\Users\Administrator\Desktop\telegram\zip\000001.zip
        String zipPath = fileConfig.directoryNameToZipPath(directoryName);

        // 遍历贴纸
        for (com.pengrad.telegrambot.model.Sticker s : stickerSet.stickers()) {
            Sticker prepareSaveSticker = stickerService.addSticker(s, stickerDirectory, BOT);
            stickerList.add(prepareSaveSticker);
            if (sticker.fileUniqueId().equals(s.fileUniqueId())) {
                mysqlSticker = prepareSaveSticker;
            }
        }
        // 添加 zip 文件
        boolean isZip = addStickerSetZip(sticker.setName(), stickerDirectory, zipPath, stickerList);
        stickerService.saveBatch(stickerList);
        stickerSetMapper.insert(new StickerSet(
                stickerSet.name(),
                stickerSet.title(),
                stickerSet.isAnimated(),
                stickerSet.containsMasks(),
                isZip ? zipPath : null,
                directoryName
        ));
        return mysqlSticker;
    }

    @Override
    public Sticker refreshStickerSet(com.pengrad.telegrambot.model.Sticker sticker, final TelegramBot BOT) throws IOException {
        StickerSet mysqlStickerSet = this.getStickerSetBySetName(sticker.setName());
        if (null == mysqlStickerSet) {
            return this.addStickerSet(sticker, BOT);
        }

        Sticker mysqlSticker = null;
        List<Sticker> stickerList = new ArrayList<>();
        // 000001
        String directoryName = mysqlStickerSet.getDirectoryName();
        // C:\Users\Administrator\Desktop\telegram\sticker
        String stickerDirectory = fileConfig.directoryNameToCanonicalPath(directoryName);
        // C:\Users\Administrator\Desktop\telegram\zip\000001.zip
        String zipPath = fileConfig.directoryNameToZipPath(directoryName);

        // 获取完整贴纸集
        com.pengrad.telegrambot.model.StickerSet stickerSet = TelegramUtils.getStickerSet(sticker.setName(), BOT);
        for (com.pengrad.telegrambot.model.Sticker s : stickerSet.stickers()) {
            // 判断贴纸是否存在
            if (null == stickerService.getStickerByFileUniqueId(s.fileUniqueId())) {
                Sticker prepareSaveSticker = stickerService.addSticker(s, stickerDirectory, BOT);
                stickerList.add(prepareSaveSticker);
                if (sticker.fileUniqueId().equals(s.fileUniqueId())) {
                    mysqlSticker = prepareSaveSticker;
                }
            }
        }
        // 添加 zip 文件
        addStickerSetZip(sticker.setName(), stickerDirectory, zipPath, stickerList);
        stickerService.saveBatch(stickerList);
        mysqlStickerSet.setIsRefresh(true);
        stickerSetMapper.updateById(UpdateUtil.copyField(mysqlStickerSet, StickerSet::getStickerSetId, StickerSet::getIsRefresh));
        return mysqlSticker;
    }

    private boolean addStickerSetZip(String setName, String stickerDirectory, String zipPath, List<Sticker> stickerList) throws IOException {
        // 获取贴纸文件
        List<File> fileList = new ArrayList<>();
        List<Sticker> mysqlStickerList = stickerService.getStickersBySetName(setName);
        mysqlStickerList.forEach(sticker -> {
            if (StringUtils.hasText(sticker.getLocalhostPath()) && sticker.getLocalhostPath().endsWith(".jpg")) {
                fileList.add(new File(sticker.getLocalhostPath()));
            }
        });
        stickerList.forEach(sticker -> {
            if (StringUtils.hasText(sticker.getLocalhostPath()) && sticker.getLocalhostPath().endsWith(".jpg")) {
                fileList.add(new File(sticker.getLocalhostPath()));
            }
        });

        // 创建描述文件
        File setNameFile = new File(ZipUtils.getSetNameTxt(stickerDirectory));
        if (!setNameFile.exists()) {
            try (OutputStream outputStream = new FileOutputStream(setNameFile)) {
                outputStream.write(setName.getBytes());
                outputStream.flush();
            }
        }
        fileList.add(setNameFile);

        // 压缩文件
        if (fileList.size() > 1) {// 只有一个 setName.txt 没有文件时不压缩
            ZipUtils.toZip(new File(zipPath), fileList);
            return true;
        }
        return false;
    }

}
