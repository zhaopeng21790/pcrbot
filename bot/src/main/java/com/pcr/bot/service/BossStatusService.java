package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.BossStatusEntity;

import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-03 18:15:56
 */
public interface BossStatusService extends IService<BossStatusEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BossStatusEntity getCurrentBossStatus(String groupCode);

}

