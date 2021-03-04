package io.lihongbin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lihongbin.entity.StickerSet;
import io.lihongbin.mapper.StickerSetMapper;
import io.lihongbin.service.StickerSetService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Li Hong Bin
 * @since 2021-03-01
 */
@Service
public class StickerSetServiceImpl extends ServiceImpl<StickerSetMapper, StickerSet> implements StickerSetService {

    private StickerSetMapper stickerSetMapper;

    public StickerSetServiceImpl(StickerSetMapper stickerSetMapper) {
        this.stickerSetMapper = stickerSetMapper;
    }

    @Override
    public void insert(StickerSet stickerSet) {
        stickerSetMapper.insert(stickerSet);
    }

    @Override
    public StickerSet getStickerSetBySetName(String setName) {
        LambdaQueryWrapper<StickerSet> queryWrapper = new QueryWrapper<StickerSet>().lambda();
        queryWrapper.eq(StickerSet::getName, setName);
        return stickerSetMapper.selectOne(queryWrapper);
    }

}
