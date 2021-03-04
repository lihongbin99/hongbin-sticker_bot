package io.lihongbin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lihongbin.entity.Sticker;
import io.lihongbin.mapper.StickerMapper;
import io.lihongbin.service.StickerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
@Service
public class StickerServiceImpl extends ServiceImpl<StickerMapper, Sticker> implements StickerService {

    private StickerMapper stickerMapper;

    public StickerServiceImpl(StickerMapper stickerMapper) {
        this.stickerMapper = stickerMapper;
    }

    @Override
    public void insert(Sticker sticker) {
        stickerMapper.insert(sticker);
    }

    @Override
    public Sticker getStickerByFileUniqueId(String fileUniqueId) {
        LambdaQueryWrapper<Sticker> queryWrapper = new QueryWrapper<Sticker>().lambda();
        queryWrapper.eq(Sticker::getFileUniqueId, fileUniqueId);
        return stickerMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Sticker> getStickersBySetName(String setName) {
        LambdaQueryWrapper<Sticker> queryWrapper = new QueryWrapper<Sticker>().lambda();
        queryWrapper.eq(Sticker::getSetName, setName);
        return stickerMapper.selectList(queryWrapper);
    }

}
