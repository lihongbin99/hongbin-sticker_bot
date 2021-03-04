package io.lihongbin.config;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig implements InitializingBean {
    private TelegramBot bot;

    private String botToken;

    public final OkHttpClient okHttpClient;

    public TelegramConfig(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public void afterPropertiesSet() {
        if (!StringUtils.hasText(this.botToken)) {
            throw new RuntimeException("请配置botToken");
        }
        log.info("botToken: {}", this.botToken);
        this.bot = new TelegramBot.Builder(this.botToken).okHttpClient(this.okHttpClient).build();
    }
}
