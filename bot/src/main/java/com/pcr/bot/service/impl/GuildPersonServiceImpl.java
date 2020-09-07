package com.pcr.bot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.dao.GuildPersonDao;
import com.pcr.bot.entity.GuildPersonEntity;
import com.pcr.bot.service.GuildPersonService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("guildPersonService")
public class GuildPersonServiceImpl extends ServiceImpl<GuildPersonDao, GuildPersonEntity> implements GuildPersonService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GuildPersonEntity> page = this.page(
                new Query<GuildPersonEntity>().getPage(params),
                new QueryWrapper<GuildPersonEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<GuildPersonEntity> getAllPersonInGroup(String groupCode) {
        QueryWrapper<GuildPersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isExist(String groupCode, String qqCode) {
        QueryWrapper<GuildPersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean removeGuildPersons(List<GuildPersonEntity> guildEntityList) {
        QueryWrapper<GuildPersonEntity> queryWrapper = new QueryWrapper<>();
        GuildPersonEntity personEntity1 = guildEntityList.get(0);
        List<String> ids = guildEntityList.stream().map(GuildPersonEntity::getQqCode).collect(Collectors.toList());
        queryWrapper.eq("group_code", personEntity1.getGroupCode());
        queryWrapper.in("qq_code", ids);
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean removeGuildPerson(String groupCode, String qqCode) {
        QueryWrapper<GuildPersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean saveUsers(List<GuildPersonEntity> personEntities) {
        try {
            return saveOrUpdateBatch(personEntities);
        } catch (DuplicateKeyException e) {
            return true;
        }
    }



}
