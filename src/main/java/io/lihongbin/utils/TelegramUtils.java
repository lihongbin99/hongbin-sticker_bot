package io.lihongbin.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.StickerSet;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetStickerSet;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetStickerSetResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;

public class TelegramUtils {

    public static String getFullFilePath(String fileId, TelegramBot BOT) {
        GetFile request = new GetFile(fileId);
        GetFileResponse getFileResponse = BOT.execute(request);
        File file = getFileResponse.file();
        return BOT.getFullFilePath(file);
    }

    public static ResponseBody getResponseBody(String fileId, final TelegramBot BOT, OkHttpClient okHttpClient) throws IOException {
        return okHttpClient.newCall(new Request.Builder().url(getFullFilePath(fileId, BOT)).build()).execute().body();
    }

    public static StickerSet getStickerSet(String stickerName, final TelegramBot BOT) {
        GetStickerSet getStickerSet = new GetStickerSet(stickerName);
        GetStickerSetResponse response = BOT.execute(getStickerSet);
        return response.stickerSet();
    }

}
