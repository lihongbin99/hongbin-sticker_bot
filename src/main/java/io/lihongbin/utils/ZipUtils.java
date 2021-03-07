package io.lihongbin.utils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void toZip(File file, List<File> fileList) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("打包 zip 失败: 删除旧 zip 失败");
        }
        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(file));
        for (File f : fileList) {
            addFile(outputStream, f);
        }
        outputStream.close();
    }

    public static void addFile(ZipOutputStream outputStream , File file) throws IOException{
        outputStream.putNextEntry(new ZipEntry(file.getName()));
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        int len;
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        inputStream.close();
        outputStream.closeEntry();
    }

    public static String getSetNameTxt(String stickerDirectory) {
        return stickerDirectory + File.separator + "setName.txt";
    }
}
