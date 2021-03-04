package io.lihongbin.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.lihongbin.base.ExecuteInterface;
import io.lihongbin.service.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UpdateServiceImpl implements UpdateService {

    private TextMessageService textMessageService;

    private StickerMessageService stickerMessageService;

    private PhotoService photoService;

    private OtherMessageService otherMessageService;

    public UpdateServiceImpl(TextMessageService textMessageService,
                             StickerMessageService stickerMessageService,
                             PhotoService photoService,
                             OtherMessageService otherMessageService) {
        this.textMessageService = textMessageService;
        this.stickerMessageService = stickerMessageService;
        this.photoService = photoService;
        this.otherMessageService = otherMessageService;
    }

    @Override
    public void message(Update update, final TelegramBot BOT) throws IOException {
        this.getExecuteInterface(update.message()).message(update, BOT);
    }

    @Override
    public void callbackQuery(Update update, final TelegramBot BOT) throws IOException {
        this.getExecuteInterface(update.callbackQuery().message()).callbackQuery(update, BOT);
    }

    private ExecuteInterface getExecuteInterface(Message message) {
        if (null != message.text()) return textMessageService;
        if (null != message.sticker()) return stickerMessageService;
        if (null != message.photo()) return photoService;
        return otherMessageService;
    }

}
