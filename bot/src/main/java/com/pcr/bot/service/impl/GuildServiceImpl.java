package com.pcr.bot.service.impl;

import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.entity.QqGroupAdmEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.pcr.bot.dao.GuildDao;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.service.GuildService;


@Service("guildService")
public class GuildServiceImpl extends ServiceImpl<GuildDao, GuildEntity> implements GuildService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GuildEntity> page = this.page(
                new Query<GuildEntity>().getPage(params),
                new QueryWrapper<GuildEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public boolean isExist(String groupCode) {
        QueryWrapper<GuildEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }



    @Override
    public GuildEntity getGuild(String groupCode) {
        QueryWrapper<GuildEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isExpired(String groupCode) {
        GuildEntity guild = getGuild(groupCode);
        if (guild == null) return false;
        Date currentTime = new Date();
        Date expiredTime = guild.getExpiredTime();
        return expiredTime.before(currentTime);
    }

    @Override
    public List<GuildEntity> findAll(Integer rate) {
        QueryWrapper<GuildEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rate", rate);
        return baseMapper.selectList(queryWrapper);
    }



}
