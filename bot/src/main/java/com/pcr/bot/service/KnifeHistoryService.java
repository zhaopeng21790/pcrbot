package com.pcr.bot.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.KnifeHistoryEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-04 11:44:14
 */
public interface KnifeHistoryService extends IService<KnifeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询最后一次的出刀记录
     */
    KnifeHistoryEntity lastKnifeHistory(String groupCode, String qqCode);

    /**
     * 查询群组中某天出刀
     */
    List<KnifeHistoryEntity> getKnifeHistory(String groupCode, String day);

    /**
     * 查询群组中某个QQ某天出刀
     */
    List<KnifeHistoryEntity> getKnifeHistory(String groupCode, String qqCode, String day);



    /**
     * 查询群组中某天已出完刀的下班人员
     */
    List<KnifeHistoryEntity> getKnifeHistoryOffDuty(String groupCode, String day);

}

