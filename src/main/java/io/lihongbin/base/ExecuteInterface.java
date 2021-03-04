package io.lihongbin.base;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import java.io.IOException;

public interface ExecuteInterface {

    void message(Update update, final TelegramBot BOT) throws IOException;

    void callbackQuery(Update update, final TelegramBot BOT) throws IOException;

}
