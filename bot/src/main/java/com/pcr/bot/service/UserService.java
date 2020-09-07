package com.pcr.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pcr.bot.entity.UserEntity;

/**
 * @author Codi
 * @date 2020/8/14
 **/
public interface UserService extends IService<UserEntity> {

    UserEntity isExists(String username);

    UserEntity save(String username, String nickname, String password);

    boolean updatePassword(String username, String password);

}
