package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.KnifeEntity;

import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-03 18:15:56
 */
public interface KnifeService extends IService<KnifeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean isExists(String groupCode, String qqCode);

    boolean isExists(String groupCode);

    KnifeEntity getCurrentKnife(String groupCode);

    /**
     * 删除群组出刀的状态
     */
    void removeKnife(String groupCode);

    void removeKnife(String groupCode, String qqCode);



}

