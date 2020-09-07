package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.GuildEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 16:37:57
 */
public interface GuildService extends IService<GuildEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean isExist(String groupCode);

    GuildEntity getGuild(String groupCode);

    boolean isExpired(String groupCode);

    List<GuildEntity> findAll(Integer rate);


}

