package com.pcr.bot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.dao.KnifeHistoryDao;
import com.pcr.bot.entity.KnifeHistoryEntity;
import com.pcr.bot.service.KnifeHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("knifeHistoryService")
public class KnifeHistoryServiceImpl extends ServiceImpl<KnifeHistoryDao, KnifeHistoryEntity> implements KnifeHistoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<KnifeHistoryEntity> page = this.page(
                new Query<KnifeHistoryEntity>().getPage(params),
                new QueryWrapper<KnifeHistoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public KnifeHistoryEntity lastKnifeHistory(String groupCode, String qqCode) {
        QueryWrapper<KnifeHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        if (qqCode != null) {
            queryWrapper.eq("qq_code", qqCode);
            queryWrapper.orderByDesc("seq_id");
            queryWrapper.orderByDesc("id");
        }else {
            queryWrapper.orderByDesc("id");
        }
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<KnifeHistoryEntity> getKnifeHistory(String groupCode, String day) {
        QueryWrapper<KnifeHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("day", day);
        queryWrapper.orderByDesc("id");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<KnifeHistoryEntity> getKnifeHistory(String groupCode, String qqCode, String day) {
        QueryWrapper<KnifeHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("day", day);
        queryWrapper.orderByAsc("seq_id");
        queryWrapper.orderByAsc("id");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<KnifeHistoryEntity> getKnifeHistoryOffDuty(String groupCode, String day) {
        QueryWrapper<KnifeHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("day", day);
        queryWrapper.eq("is_reward_knife", 0);
        queryWrapper.eq("seq_id", 3);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }


}
