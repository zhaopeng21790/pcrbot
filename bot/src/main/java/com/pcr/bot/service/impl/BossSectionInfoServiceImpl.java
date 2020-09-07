package com.pcr.bot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.common.utils.Query;
import com.pcr.bot.dao.BossSectionInfoDao;
import com.pcr.bot.entity.BossSectionInfoEntity;
import com.pcr.bot.service.BossSectionInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("bossSectionInfoService")
public class BossSectionInfoServiceImpl extends ServiceImpl<BossSectionInfoDao, BossSectionInfoEntity> implements BossSectionInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BossSectionInfoEntity> page = this.page(
                new Query<BossSectionInfoEntity>().getPage(params),
                new QueryWrapper<BossSectionInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public BossSectionInfoEntity getBossSectionInfo(Integer section, Integer bossNum, Integer rate) {
        QueryWrapper<BossSectionInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section", section);
        queryWrapper.eq("boss_num", bossNum);
        queryWrapper.eq("rate", rate);
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

}
