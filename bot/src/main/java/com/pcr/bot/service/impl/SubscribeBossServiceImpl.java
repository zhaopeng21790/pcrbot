package com.pcr.bot.service.impl;

import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.entity.GuildEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.dao.SubscribeBossDao;
import com.pcr.bot.entity.SubscribeBossEntity;
import com.pcr.bot.service.SubscribeBossService;


@Service("subscribeBossService")
public class SubscribeBossServiceImpl extends ServiceImpl<SubscribeBossDao, SubscribeBossEntity> implements SubscribeBossService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SubscribeBossEntity> page = this.page(
                new Query<SubscribeBossEntity>().getPage(params),
                new QueryWrapper<SubscribeBossEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean isSubscribed(String groupCode, String qqCode, Integer number) {
        QueryWrapper<SubscribeBossEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("boss_num", number);
        queryWrapper.eq("subscribe_time", DateUtil.getPCRDateOfDayString());
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean unSubscribe(String groupCode, String qqCode) {
        QueryWrapper<SubscribeBossEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("subscribe_time", DateUtil.getPCRDateOfDayString());
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean unSubscribe(String groupCode, String qqCode, Integer number) {
        QueryWrapper<SubscribeBossEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("boss_num", number);
        queryWrapper.eq("subscribe_time", DateUtil.getPCRDateOfDayString());
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public List<SubscribeBossEntity> getSubscribed(String groupCode, Integer number) {
        QueryWrapper<SubscribeBossEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("boss_num", number);
        queryWrapper.eq("subscribe_time", DateUtil.getPCRDateOfDayString());
        queryWrapper.orderByAsc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SubscribeBossEntity> getSubscribed(String groupCode) {
        QueryWrapper<SubscribeBossEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("subscribe_time", DateUtil.getPCRDateOfDayString());
        return baseMapper.selectList(queryWrapper);
    }


}
