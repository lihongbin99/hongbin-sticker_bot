package io.lihongbin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetFile;
import io.lihongbin.config.FileConfig;
import io.lihongbin.config.TelegramConfig;
import io.lihongbin.entity.Sticker;
import io.lihongbin.mapper.StickerMapper;
import io.lihongbin.service.StickerService;
import io.lihongbin.utils.FileUtils;
import io.lihongbin.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
public class StickerServiceImpl extends ServiceImpl<StickerMapper, Sticker> implements StickerService {

    private StickerMapper stickerMapper;

    private TelegramConfig telegramConfig;

    private FileConfig fileConfig;

    public StickerServiceImpl(StickerMapper stickerMapper, TelegramConfig telegramConfig, FileConfig fileConfig) {
        this.stickerMapper = stickerMapper;
        this.telegramConfig = telegramConfig;
        this.fileConfig = fileConfig;
    }

    @Override
    public void insert(Sticker sticker) {
        stickerMapper.insert(sticker);
    }

    @Override
    public Sticker getStickerByFileUniqueId(String fileUniqueId) {
        LambdaQueryWrapper<Sticker> queryWrapper = new QueryWrapper<Sticker>().lambda();
        queryWrapper.eq(Sticker::getFileUniqueId, fileUniqueId);
        return stickerMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Sticker> getStickersBySetName(String setName) {
        LambdaQueryWrapper<Sticker> queryWrapper = new QueryWrapper<Sticker>().lambda();
        queryWrapper.eq(Sticker::getSetName, setName);
        return stickerMapper.selectList(queryWrapper);
    }

    @Override
    public Sticker addSticker(com.pengrad.telegrambot.model.Sticker s, String stickerDirectory, final TelegramBot BOT) throws IOException {
        Sticker prepareSaveSticker = null;
        // 下载贴纸文件
        String fullPath = BOT.getFullFilePath(BOT.execute(new GetFile(s.fileId())).file());
        String suffix = FileUtils.getFileSuffix(fullPath);
        ResponseBody body = telegramConfig.okHttpClient.newCall(new Request.Builder().url(fullPath).build()).execute().body();
        if (null != body) {
            byte[] bytes = body.bytes();
            // 保存原文件
            String fileName = s.fileUniqueId() + "-" + UUID.get() + ".";
            File stickerFile = FileUtils.getStickerFile(stickerDirectory, fileName, suffix);
            try (OutputStream outputStream = new FileOutputStream(stickerFile)) {
                outputStream.write(bytes);
                outputStream.flush();
            }
            String localhostPath = stickerFile.getCanonicalPath();

            if (!s.isAnimated() && localhostPath.endsWith(".webp")) {
                try {
                    File png = FileUtils.getStickerFile(stickerDirectory, fileName, "png");
                    File jpg = FileUtils.getStickerFile(stickerDirectory, fileName, "jpg");

                    Process exec = Runtime.getRuntime().exec(new String[] {fileConfig.getDwebp(), localhostPath, "-o", png.getCanonicalPath()});
                    int i = exec.waitFor();
                    if (i == 0) {
                        BufferedImage bufferedImage = ImageIO.read(png);
                        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                        ImageIO.write(newBufferedImage, "jpg", jpg);
                    }
                    localhostPath = jpg.getCanonicalPath();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            prepareSaveSticker = new Sticker(
                    s.setName(),// setName
                    s.emoji(),// emoji
                    s.isAnimated(),// isAnimated
                    s.fileUniqueId(),// fileUniqueId
                    s.height(),// height
                    s.width(),// width
                    s.fileSize(),// fileSize
                    localhostPath// localhostPath
            );
        } else {
            log.error("获取{}贴纸集的{}贴纸失败", s.setName(), s.fileUniqueId());
        }
        return prepareSaveSticker;
    }

}
