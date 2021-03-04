package io.lihongbin.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import io.lihongbin.service.UpdateService;
import org.springframework.stereotype.Component;

@Component
public class BotStartRun {

    private UpdateService updateService;

    public BotStartRun(UpdateService updateService) {
        this.updateService = updateService;
    }

    public void run(final TelegramBot BOT) {
        BOT.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                try {

                    if (null != update.message()) {
                        this.updateService.message(update, BOT);
                    }
                    else if (null != update.callbackQuery()) {
                        this.updateService.callbackQuery(update, BOT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

}
