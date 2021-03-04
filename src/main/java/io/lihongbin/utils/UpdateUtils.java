package io.lihongbin.utils;

import com.pengrad.telegrambot.model.Update;

public class UpdateUtils {

    public static Long getMessageChatId(Update update) {
        return update.message().chat().id();
    }

    public static Long getCallbackQueryChatId(Update update) {
        return update.callbackQuery().message().chat().id();
    }

}
