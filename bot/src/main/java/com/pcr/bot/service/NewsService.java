package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.entity.NewsEntity;

import java.util.List;

/**
 * @author Codi
 * @date 2020/8/14
 **/
public interface NewsService extends IService<NewsEntity> {

    boolean isExistsNews(String url);

    List<NewsEntity> getNews(Integer count, Integer rate);

}
