package io.lihongbin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pengrad.telegrambot.TelegramBot;
import io.lihongbin.entity.Sticker;
import io.lihongbin.entity.StickerSet;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
public interface StickerSetService extends IService<StickerSet> {

    StickerSet getStickerSetBySetName(String setName);

    Sticker addStickerSet(com.pengrad.telegrambot.model.Sticker sticker, final TelegramBot BOT) throws IOException;

    Sticker refreshStickerSet(com.pengrad.telegrambot.model.Sticker sticker, final TelegramBot BOT) throws IOException;

}
