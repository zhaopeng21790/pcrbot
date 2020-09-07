package com.pcr.bot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.dao.TreeDao;
import com.pcr.bot.entity.TreeEntity;
import com.pcr.bot.service.TreeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("treeService")
public class TreeServiceImpl extends ServiceImpl<TreeDao, TreeEntity> implements TreeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TreeEntity> page = this.page(
                new Query<TreeEntity>().getPage(params),
                new QueryWrapper<TreeEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public void addTreeEntity(TreeEntity treeEntity) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", treeEntity.getGroupCode());
        queryWrapper.eq("qq_code", treeEntity.getQqCode());
        queryWrapper.eq("type", treeEntity.getType());
        queryWrapper.eq("day", treeEntity.getDay());
        Integer rows = baseMapper.selectCount(queryWrapper);
        if (rows == 0) saveOrUpdate(treeEntity);
    }

    @Override
    public void clearTreeOfDay(String groupCode, String day) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("type", Constant.Type.TREE.getValue());
        queryWrapper.eq("day", day);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public boolean removeTree(String groupCode, String qqCode) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }

    @Override
    public boolean removeTree(String groupCode, String qqCode, int type) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("type", type);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }


    @Override
    public List<TreeEntity> getList(String groupCode, int type) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("type", type);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<TreeEntity> getList(String groupCode, String qqCode, int type) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("type", type);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public TreeEntity getTreeEntity(String groupCode, String qqCode, int type) {
        QueryWrapper<TreeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", groupCode);
        queryWrapper.eq("qq_code", qqCode);
        queryWrapper.eq("type", type);
        queryWrapper.eq("day", DateUtil.getPCRDateOfDayString());
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

}
