package com.pcr.bot.dao;

import com.pcr.bot.entity.GuildPersonEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公会的人员
 * 
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 16:37:57
 */
@Mapper
public interface GuildPersonDao extends BaseMapper<GuildPersonEntity> {
	
}
