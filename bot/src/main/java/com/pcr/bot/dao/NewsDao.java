package com.pcr.bot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pcr.bot.entity.NewsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 16:37:57
 */
@Mapper
public interface NewsDao extends BaseMapper<NewsEntity> {

}
