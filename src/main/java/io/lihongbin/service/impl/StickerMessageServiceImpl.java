package io.lihongbin.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import io.lihongbin.constant.CallbackDataConstant;
import io.lihongbin.entity.Sticker;
import io.lihongbin.service.StickerMessageService;
import io.lihongbin.service.StickerService;
import io.lihongbin.service.StickerSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class StickerMessageServiceImpl implements StickerMessageService {

    private StickerSetService stickerSetService;

    private StickerService stickerService;

    public StickerMessageServiceImpl(StickerSetService stickerSetService, StickerService stickerService) {
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
            mysqlSticker = null == stickerSetService.getStickerSetBySetName(sticker.setName()) ?
                                        // 添加贴纸
                                        stickerSetService.addStickerSet(sticker, BOT) :
                                        // 刷新贴纸
                                        stickerSetService.refreshStickerSet(sticker, BOT);
        }

        // 返回消息
        if (null != mysqlSticker && !mysqlSticker.getIsAnimated() && mysqlSticker.getLocalhostPath().endsWith(".jpg")) {
            SendPhoto sendPhoto = new SendPhoto(chatId, new File(mysqlSticker.getLocalhostPath()));
            sendPhoto.replyToMessageId(update.message().messageId());
            sendPhoto.caption(sticker.setName());
            sendPhoto.replyMarkup(new InlineKeyboardMarkup(
                    new InlineKeyboardButton("下载完整贴纸集").callbackData(CallbackDataConstant.DOWNLOAD_STICKER.name()),
                    new InlineKeyboardButton("返回贴纸错误").callbackData(CallbackDataConstant.REFRESH_STICKER.name())
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