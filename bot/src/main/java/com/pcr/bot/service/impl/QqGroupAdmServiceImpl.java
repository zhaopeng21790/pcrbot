package com.pcr.bot.service.impl;

import com.pcr.bot.command.CommandUtils;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pcr.bot.dao.QqGroupAdmDao;
import com.pcr.bot.entity.QqGroupAdmEntity;
import com.pcr.bot.service.QqGroupAdmService;


@Service("qqGroupAdmService")
public class QqGroupAdmServiceImpl extends ServiceImpl<QqGroupAdmDao, QqGroupAdmEntity> implements QqGroupAdmService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QqGroupAdmEntity> page = this.page(
                new Query<QqGroupAdmEntity>().getPage(params),
                new QueryWrapper<QqGroupAdmEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public QqGroupAdmEntity getOne(String groupCode, String qqCode) {
        QueryWrapper<QqGroupAdmEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public int removeAdmin(String groupCode, List<String> qqCodeList) {
        QueryWrapper<QqGroupAdmEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.in("qq_code", qqCodeList);
        return baseMapper.delete(queryWrapper);
    }

    @Override
    public int removeAdmin(String groupCode) {
        QueryWrapper<QqGroupAdmEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        return baseMapper.delete(queryWrapper);
    }

    @Override
    public boolean addAdmin(String groupCode, List<String> qqCodeList) {
        List<QqGroupAdmEntity> entityList = new ArrayList<>();
        for (String qqCode : qqCodeList) {
            QqGroupAdmEntity qqGroupAdmEntity = new QqGroupAdmEntity();
            qqGroupAdmEntity.setQqCode(qqCode);
            qqGroupAdmEntity.setGroupCode(groupCode);
            boolean exist = isExist(groupCode, qqCode);
            if (!exist) {
                entityList.add(qqGroupAdmEntity);
            }
        }
        return saveBatch(entityList);
    }



    @Override
    public boolean isExist(String groupCode, String qqCode) {
        QueryWrapper<QqGroupAdmEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }


}
