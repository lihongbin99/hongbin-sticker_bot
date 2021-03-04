package io.lihongbin.service;

import io.lihongbin.entity.Sticker;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
public interface StickerService extends IService<Sticker> {

    void insert(Sticker sticker);

    Sticker getStickerByFileUniqueId(String fileUniqueId);

    List<Sticker> getStickersBySetName(String setName);

}
