package io.lihongbin.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig implements InitializingBean {

    private String stickerDirectory;

    private String zipDirectory;

    private String dwebp;

    @Override
    public void afterPropertiesSet() throws IOException {
        File stickerDirectory = new File(this.stickerDirectory);
        log.info("stickerDirectory: {}", stickerDirectory.getCanonicalPath());
        if (!stickerDirectory.exists()) {
            if (!stickerDirectory.mkdirs()) {
                throw new RuntimeException("创建文件夹失败" + stickerDirectory.getCanonicalPath());
            }
        }
        if (!stickerDirectory.isDirectory()) {
            throw new RuntimeException(stickerDirectory.getCanonicalPath() + "不是文件夹");
        }

        File zipDirectory = new File(this.zipDirectory);
        log.info("zipDirectory: {}", zipDirectory.getCanonicalPath());
        if (!zipDirectory.exists()) {
            if (!zipDirectory.mkdirs()) {
                throw new RuntimeException("创建文件夹失败" + zipDirectory.getCanonicalPath());
            }
        }
        if (!zipDirectory.isDirectory()) {
            throw new RuntimeException(zipDirectory.getCanonicalPath() + "不是文件夹");
        }

        if (!StringUtils.hasText(this.dwebp)) {
            throw new RuntimeException("请设置dwebp位置");
        }
        log.info("dwebp: {}", this.dwebp);
    }

    public String getNewStickerDirectory() throws IOException {
        int newStickerDirectoryName = 0;
        File stickerDirectoryFile = new File(this.stickerDirectory);
        File[] files = stickerDirectoryFile.listFiles();
        if (null != files) {
            for (File directory : files) {
                try {
                    int directoryName = Integer.parseInt(directory.getName());
                    if (directoryName > newStickerDirectoryName) {
                        newStickerDirectoryName = directoryName;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        StringBuilder directoryName = new StringBuilder(String.valueOf(newStickerDirectoryName + 1));
        while (directoryName.length() < 6) {
            directoryName.insert(0, "0");
        }
        File directory = new File(this.stickerDirectory + File.separator + directoryName);
        if (!directory.mkdirs()) {
            throw new RuntimeException("创建文件夹失败");
        }
        return directoryName.toString();
    }

    public String directoryNameToCanonicalPath(String directoryName) {
        return this.stickerDirectory + File.separator + directoryName;
    }

    public String directoryNameToZipPath(String directoryName) {
        return this.zipDirectory + File.separator + directoryName + ".zip";
    }
}
