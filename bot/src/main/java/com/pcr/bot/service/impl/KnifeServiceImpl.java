package com.pcr.bot.service.impl;

import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.entity.GuildEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pcr.bot.dao.KnifeDao;
import com.pcr.bot.entity.KnifeEntity;
import com.pcr.bot.service.KnifeService;


@Service("knifeService")
public class KnifeServiceImpl extends ServiceImpl<KnifeDao, KnifeEntity> implements KnifeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<KnifeEntity> page = this.page(
                new Query<KnifeEntity>().getPage(params),
                new QueryWrapper<KnifeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean isExists(String groupCode, String qqCode) {
        QueryWrapper<KnifeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean isExists(String groupCode) {
        QueryWrapper<KnifeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public KnifeEntity getCurrentKnife(String groupCode) {
        QueryWrapper<KnifeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void removeKnife(String groupCode) {
        QueryWrapper<KnifeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public void removeKnife(String groupCode, String qqCode) {
        QueryWrapper<KnifeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        baseMapper.delete(queryWrapper);
    }

}
