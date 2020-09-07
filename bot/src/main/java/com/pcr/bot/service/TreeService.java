package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.TreeEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-04 17:36:30
 */
public interface TreeService extends IService<TreeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addTreeEntity(TreeEntity treeEntity);

    void clearTreeOfDay(String groupCode, String day);

    boolean removeTree(String groupCode, String qqCode);

    boolean removeTree(String groupCode, String qqCode, int type);

    List<TreeEntity> getList(String groupCode, int type);

    List<TreeEntity> getList(String groupCode,String qqCode, int type);

    TreeEntity getTreeEntity(String groupCode, String qqCode, int type);



}

