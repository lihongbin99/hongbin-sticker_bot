package io.lihongbin.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "socks")
public class SocksConfig {

    private String proxyHost;

    private Integer proxyPort;

    @Bean
    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置代理
        if (StringUtils.hasText(this.proxyHost) && null != this.proxyHost) {
            log.info("proxy: {}:{}", this.proxyHost, this.proxyPort);
            builder.proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(this.proxyHost, this.proxyPort)));
        }
        builder.connectTimeout(3, TimeUnit.MINUTES);
        builder.readTimeout(3, TimeUnit.MINUTES);
        builder.writeTimeout(3, TimeUnit.MINUTES);
        return builder.build();
    }

}
