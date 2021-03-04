package io.lihongbin.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import io.lihongbin.config.FileConfig;
import io.lihongbin.config.TelegramConfig;
import io.lihongbin.constant.CallbackDataConstant;
import io.lihongbin.entity.Sticker;
import io.lihongbin.entity.StickerSet;
import io.lihongbin.service.StickerMessageService;
import io.lihongbin.service.StickerService;
import io.lihongbin.service.StickerSetService;
import io.lihongbin.utils.FileUtils;
import io.lihongbin.utils.TelegramUtils;
import io.lihongbin.utils.UUID;
import io.lihongbin.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StickerMessageServiceImpl implements StickerMessageService {

    private TelegramConfig telegramConfig;

    private FileConfig fileConfig;

    private StickerSetService stickerSetService;

    private StickerService stickerService;

    public StickerMessageServiceImpl(TelegramConfig telegramConfig, FileConfig fileConfig, StickerSetService stickerSetService, StickerService stickerService) {
        this.telegramConfig = telegramConfig;
        this.fileConfig = fileConfig;
        this.stickerSetService = stickerSetService;
        this.stickerService = stickerService;
    }

    /**
    *  处理贴纸
    *   1. 获取贴纸完整集
    *   2. 遍历贴纸, 保存到硬盘
    *   3. 讲数据保存到数据库
    *   4. 返回消息
     * @see PhotoServiceImpl#callbackQuery(com.pengrad.telegrambot.model.Update, com.pengrad.telegrambot.TelegramBot)
     */
    @Override
    @Transactional
    public synchronized void message(Update update, final TelegramBot BOT) throws IOException {
        Message message = update.message();
        long chatId = message.chat().id();
        com.pengrad.telegrambot.model.Sticker sticker = message.sticker();
        String fileUniqueId = sticker.fileUniqueId();

        Sticker mysqlSticker = stickerService.getStickerByFileUniqueId(fileUniqueId);
        // 保存贴纸到数据库和本地目录
        if (null == mysqlSticker) {
            List<Sticker> stickerList = new ArrayList<>();
            List<File> fileList = new ArrayList<>();
            // 获取完整贴纸集
            com.pengrad.telegrambot.model.StickerSet stickerSet = TelegramUtils.getStickerSet(sticker.setName(), BOT);

            // 000001
            String directoryName = fileConfig.getNewStickerDirectory();
            // C:\Users\Administrator\Desktop\telegram\sticker
            String stickerDirectory = fileConfig.directoryNameToCanonicalPath(directoryName);
            // C:\Users\Administrator\Desktop\telegram\zip\000001.zip
            String zipPath = fileConfig.getZipDirectory() + File.separator + directoryName + ".zip";

            // 遍历贴纸
            for (com.pengrad.telegrambot.model.Sticker s : stickerSet.stickers()) {
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
                            fileList.add(jpg);
                            localhostPath = jpg.getCanonicalPath();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Sticker prepareSaveSticker = new Sticker(
                            s.setName(),
                            s.emoji(),
                            s.isAnimated(),
                            s.fileUniqueId(),
                            s.height(),
                            s.width(),
                            s.fileSize(),
                            localhostPath
                    );
                    stickerList.add(prepareSaveSticker);
                    if (fileUniqueId.equals(s.fileUniqueId())) {
                        mysqlSticker = prepareSaveSticker;
                    }
                } else {
                    log.error("获取{}贴纸集的{}贴纸失败", s.setName(), s.fileUniqueId());
                }
            }
            // 创建描述文件
            File setNameFile = new File(stickerDirectory + File.separator + "setName.txt");
            try (OutputStream outputStream = new FileOutputStream(setNameFile)) {
                outputStream.write(sticker.setName().getBytes());
                outputStream.flush();
            }
            fileList.add(setNameFile);
            // 压缩文件
            if (fileList.size() > 1) {// 只有一个 setName.txt 没有文件时不压缩
                ZipUtils.toZip(new File(zipPath), fileList);
            }
            stickerService.saveBatch(stickerList);
            stickerSetService.insert(new StickerSet(
                    stickerSet.name(),
                    stickerSet.title(),
                    stickerSet.isAnimated(),
                    stickerSet.containsMasks(),
                    zipPath
            ));
        }

        // 返回消息
        if (null != mysqlSticker && !mysqlSticker.getIsAnimated() && mysqlSticker.getLocalhostPath().endsWith(".jpg")) {
            SendPhoto sendPhoto = new SendPhoto(chatId, new File(mysqlSticker.getLocalhostPath()));
            sendPhoto.replyToMessageId(update.message().messageId());
            sendPhoto.caption(sticker.setName());
            sendPhoto.replyMarkup(new InlineKeyboardMarkup(
                    new InlineKeyboardButton("下载完整贴纸集")
                            .callbackData(CallbackDataConstant.DOWNLOAD_STICKER.name())
            ));
            BOT.execute(sendPhoto);
        } else {
            BOT.execute(new SendMessage(chatId, "此格式暂不支持下载").replyToMessageId(update.message().messageId()));
        }
    }

    @Override
    public void callbackQuery(Update update, final TelegramBot BOT) throws IOException {
    }

}