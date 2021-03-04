package io.lihongbin.service;

import io.lihongbin.entity.StickerSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
public interface StickerSetService extends IService<StickerSet> {

    void insert(StickerSet stickerSet);

    StickerSet getStickerSetBySetName(String setName);

}
