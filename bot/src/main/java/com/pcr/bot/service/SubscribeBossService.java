package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.SubscribeBossEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 23:05:15
 */
public interface SubscribeBossService extends IService<SubscribeBossEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean isSubscribed(String groupCode, String qqCode, Integer number);

    boolean unSubscribe(String groupCode, String qqCode);

    boolean unSubscribe(String groupCode, String qqCode, Integer number);

    List<SubscribeBossEntity> getSubscribed(String groupCode, Integer number);
    // 获取全部
    List<SubscribeBossEntity> getSubscribed(String groupCode);


}

