package com.pcr.bot.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.JwtUtils;
import com.pcr.bot.dao.UserDao;
import com.pcr.bot.entity.UserEntity;
import com.pcr.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Codi
 * @date 2020/8/14
 **/
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public UserEntity isExists(String username) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.last("limit 1");
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        return userEntity;

    }

    @Override
    public UserEntity save(String username, String nickname, String password) {
        //查询QQ对应的昵称
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setNickName(nickname);
        userEntity.setPassword(MD5.create().digestHex(password));
        String token = jwtUtils.generateToken(username);
        userEntity.setToken(token);
        baseMapper.insert(userEntity);
        return userEntity;
    }

    @Override
    public boolean updatePassword(String username, String password) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(MD5.create().digestHex(password));
        queryWrapper.eq("username", username);
        int rows = baseMapper.update(userEntity, queryWrapper);
        return rows > 0;
    }

}
