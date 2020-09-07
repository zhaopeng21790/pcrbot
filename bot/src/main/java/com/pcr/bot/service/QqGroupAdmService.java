package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.common.utils.PageUtils;
import com.pcr.bot.entity.QqGroupAdmEntity;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * QQ群组的管理员
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 17:03:05
 */
public interface QqGroupAdmService extends IService<QqGroupAdmEntity> {

    PageUtils queryPage(Map<String, Object> params);

    QqGroupAdmEntity getOne(String groupCode, String qqCode);

    int removeAdmin(String groupCode, List<String> qqCodeList);

    int removeAdmin(String groupCode);

    boolean addAdmin(String groupCode, List<String> qqCodeList);

    boolean isExist(String groupCode, String qqCode);
}

