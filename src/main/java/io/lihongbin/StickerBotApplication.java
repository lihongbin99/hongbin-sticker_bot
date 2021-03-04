package io.lihongbin;

import io.lihongbin.config.TelegramConfig;
import io.lihongbin.core.BotStartRun;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "io.lihongbin.mapper")
public class StickerBotApplication implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(StickerBotApplication.class, args);
    }

    private TelegramConfig telegramConfig;

    private BotStartRun botStartRun;

    public StickerBotApplication(TelegramConfig telegramConfig, BotStartRun botStartRun) {
        this.telegramConfig = telegramConfig;
        this.botStartRun = botStartRun;
    }

    @Override
    public void afterPropertiesSet() {
        botStartRun.run(telegramConfig.getBot());
    }

}
