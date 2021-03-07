package io.lihongbin.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import io.lihongbin.constant.CallbackDataConstant;
import io.lihongbin.entity.StickerSet;
import io.lihongbin.service.PhotoService;
import io.lihongbin.service.StickerSetService;
import io.lihongbin.utils.UpdateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Service
public class PhotoServiceImpl implements PhotoService {

    private StickerSetService stickerSetService;

    public PhotoServiceImpl(StickerSetService stickerSetService) {
        this.stickerSetService = stickerSetService;
    }

    @Override
    public void message(Update update, final TelegramBot BOT) throws IOException {
    }

    @Override
    public void callbackQuery(Update update, final TelegramBot BOT) throws IOException {
        String data = update.callbackQuery().data();
        Long chatId = UpdateUtils.getCallbackQueryChatId(update);

        // 处理消息
        if (CallbackDataConstant.DOWNLOAD_STICKER.name().equals(data)) {
            String setName = update.callbackQuery().message().caption();
            StickerSet stickerSet = stickerSetService.getStickerSetBySetName(setName);
            if (StringUtils.hasText(stickerSet.getZipPath())) {
                SendDocument sendDocument = new SendDocument(chatId, new File(stickerSet.getZipPath()));
                sendDocument.caption(setName);
                BOT.execute(sendDocument);
            } else {
                BOT.execute(new SendMessage(chatId, "此格式暂不支持下载").replyToMessageId(update.message().messageId()));
            }
        } else if (CallbackDataConstant.REFRESH_STICKER.name().equals(data)) {
            // TODO 刷新贴纸
        }
    }

}
