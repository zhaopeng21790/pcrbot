package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.BossSectionInfoEntity;

import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-03 19:51:50
 */
public interface BossSectionInfoService extends IService<BossSectionInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BossSectionInfoEntity getBossSectionInfo(Integer section, Integer bossNum, Integer rate);
}

