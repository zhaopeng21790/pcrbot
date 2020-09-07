package com.pcr.bot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.dao.NewsDao;
import com.pcr.bot.entity.NewsEntity;
import com.pcr.bot.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Codi
 * @date 2020/8/14
 **/
@Service("newsService")
public class NewsServiceImpl extends ServiceImpl<NewsDao, NewsEntity> implements NewsService {


    @Override
    public boolean isExistsNews(String url) {
        QueryWrapper<NewsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        Integer integer = baseMapper.selectCount(queryWrapper);
        return integer > 0;
    }

    @Override
    public List<NewsEntity> getNews(Integer count, Integer rate) {
        QueryWrapper<NewsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("environment", rate);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit " + count);
        return baseMapper.selectList(queryWrapper);
    }
}
