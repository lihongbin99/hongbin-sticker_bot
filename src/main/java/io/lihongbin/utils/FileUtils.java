package io.lihongbin.utils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static String getFileSuffix(String file) {
        String  suffix = null;
        int index;
        if (StringUtils.hasText(file) && (index = file.lastIndexOf(".")) != -1 && file.length() > index + 1) {
            suffix = file.substring(index + 1);
        }
        return suffix;
    }

    public static void mkdirsDirectory(File file) throws IOException {
        if (file.getParentFile().isDirectory() || (!file.exists() && file.getParentFile().mkdirs())) {
            return;
        }
        throw new RuntimeException("文件夹创建失败: " + file.getCanonicalPath());
    }

    public static File getStickerFile(String stickerDirectory, String fileName, String suffix) throws IOException {
        File stickerFile = new File(stickerDirectory + File.separator + suffix + File.separator + fileName + suffix);
        FileUtils.mkdirsDirectory(stickerFile);
        return stickerFile;
    }

}
