package com.pcr.bot.service.impl;

import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.entity.SubscribeBossEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pcr.bot.dao.GroupStatusDao;
import com.pcr.bot.entity.GroupStatusEntity;
import com.pcr.bot.service.GroupStatusService;


@Service("groupStatusService")
public class GroupStatusServiceImpl extends ServiceImpl<GroupStatusDao, GroupStatusEntity> implements GroupStatusService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GroupStatusEntity> page = this.page(
                new Query<GroupStatusEntity>().getPage(params),
                new QueryWrapper<GroupStatusEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void lock(String groupCode) {
        GroupStatusEntity entity = new GroupStatusEntity();
        entity.setGroupCode(groupCode);
        entity.setStatus(Constant.Lock.lock.getValue());
        saveOrUpdate(entity);
    }

    @Override
    public void unLock(String groupCode) {
        GroupStatusEntity entity = new GroupStatusEntity();
        entity.setGroupCode(groupCode);
        entity.setStatus(Constant.Lock.unlock.getValue());
        saveOrUpdate(entity);
    }

    @Override
    public GroupStatusEntity getGroupStatusEntity(String groupCode) {
        QueryWrapper<GroupStatusEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

}
