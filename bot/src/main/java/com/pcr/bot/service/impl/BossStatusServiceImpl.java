package com.pcr.bot.service.impl;

import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.entity.GroupStatusEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pcr.bot.dao.BossStatusDao;
import com.pcr.bot.entity.BossStatusEntity;
import com.pcr.bot.service.BossStatusService;


@Service("bossStatusService")
public class BossStatusServiceImpl extends ServiceImpl<BossStatusDao, BossStatusEntity> implements BossStatusService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BossStatusEntity> page = this.page(
                new Query<BossStatusEntity>().getPage(params),
                new QueryWrapper<BossStatusEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public BossStatusEntity getCurrentBossStatus(String groupCode) {
        QueryWrapper<BossStatusEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
//        String ofMonthString = DateUtil.getCuttentDateOfMonthString();
//        queryWrapper.eq("guild_date", ofMonthString);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

}
