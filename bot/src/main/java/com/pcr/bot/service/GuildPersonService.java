package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.entity.GuildPersonEntity;

import java.util.List;
import java.util.Map;

/**
 * 公会的人员
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 16:37:57
 */
public interface GuildPersonService extends IService<GuildPersonEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<GuildPersonEntity> getAllPersonInGroup(String groupCode);

    boolean saveUsers(List<GuildPersonEntity> personEntities);

    boolean isExist(String groupCode, String qqCode);

    boolean removeGuildPersons(List<GuildPersonEntity> guildEntityList);

    boolean removeGuildPerson(String groupCode, String qqCode);

}

