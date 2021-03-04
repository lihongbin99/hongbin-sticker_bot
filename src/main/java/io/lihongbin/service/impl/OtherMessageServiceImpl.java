package io.lihongbin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.lihongbin.config.GlobalConfig;
import io.lihongbin.service.OtherMessageService;
import io.lihongbin.utils.UpdateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OtherMessageServiceImpl implements OtherMessageService {

    @Override
    public void message(Update update, TelegramBot BOT) {
        String s = JSONObject.toJSONString(update, GlobalConfig.SERIALIZE_CONFIG);
        log.info("接收到未知类型信息: {}", s);
        BOT.execute(new SendMessage(UpdateUtils.getMessageChatId(update), s));
    }

    @Override
    public void callbackQuery(Update update, TelegramBot BOT) {
        String s = JSONObject.toJSONString(update, GlobalConfig.SERIALIZE_CONFIG);
        log.info("接收到未知类型信息: {}", s);
        BOT.execute(new SendMessage(UpdateUtils.getCallbackQueryChatId(update), s));
    }

}
