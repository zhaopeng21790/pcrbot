package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.GroupStatusEntity;

import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-03 10:50:52
 */
public interface GroupStatusService extends IService<GroupStatusEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void lock(String groupCode);

    void unLock(String groupCode);

    GroupStatusEntity getGroupStatusEntity(String groupCode);
}

