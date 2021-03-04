package io.lihongbin.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import io.lihongbin.service.TextMessageService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TextMessageServiceImpl implements TextMessageService {

    @Override
    public void message(Update update, TelegramBot BOT) throws IOException {
    }

    @Override
    public void callbackQuery(Update update, TelegramBot BOT) throws IOException {
    }
}
