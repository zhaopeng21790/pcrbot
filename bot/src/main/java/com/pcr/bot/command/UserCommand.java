package com.pcr.bot.command;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.entity.UserEntity;
import com.pcr.bot.service.UserService;
import com.simbot.component.mirai.messages.Reply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Codi
 * @date 2020/8/21
 **/
@Slf4j
@Component
public class UserCommand {
    @Autowired
    UserService userService;

    public Reply registerUser(PrivateMsg privateMsg, MsgSender sender) {
        // thisCode 代表当前接收到消息的机器人账号。
        final String botCode = privateMsg.getThisCode();
        // 发消息人的昵称
        final String nickname = privateMsg.getNickname();
        // 发消息人的账号
        final String code = privateMsg.getCode();
        // 发消息人发的消息
        final String msg = privateMsg.getMsg();
        // 由于拼接的东西比较长，用java自带的MessageFormat对消息进行格式化，会比较直观
        final MessageFormat message = new MessageFormat("机器人{0}接收到了{1}({2})的私信消息：{3}");
        final String printMsg = message.format(new Object[]{botCode, nickname, code, msg});
        log.debug(printMsg);
        // 红色显眼儿一点
        UserEntity exists = userService.isExists(code);
        if (exists != null) throw new RRException("用户已存在");
        String password = msg.replace("注册-", "");
        userService.save(code, nickname, password);
        String sendmsg = "用户注册成功：\n"  + "密码：" + password;
        return Reply.getMessageReply(sendmsg);
    }


    public Reply resetPassword(PrivateMsg privateMsg, MsgSender sender) {
        // thisCode 代表当前接收到消息的机器人账号。
        final String botCode = privateMsg.getThisCode();
        // 发消息人的昵称
        final String nickname = privateMsg.getNickname();
        // 发消息人的账号
        final String code = privateMsg.getCode();
        // 发消息人发的消息
        final String msg = privateMsg.getMsg();
        // 由于拼接的东西比较长，用java自带的MessageFormat对消息进行格式化，会比较直观
        final MessageFormat message = new MessageFormat("机器人{0}接收到了{1}({2})的私信消息：{3}");
        final String printMsg = message.format(new Object[]{botCode, nickname, code, msg});
        // 红色显眼儿一点
        log.debug(printMsg);
        UserEntity exists = userService.isExists(code);
        if (exists == null) throw new RRException("用户不存在，请先注册");
        String password = msg.replace("重置密码-", "");
        userService.updatePassword(code, password);
        String sendmsg = "密码重置成功：" + "\n密码：" + password;
        return Reply.getMessageReply(sendmsg);

    }
}
