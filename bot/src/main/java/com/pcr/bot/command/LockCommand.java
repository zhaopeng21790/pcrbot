package com.pcr.bot.command;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.entity.GroupStatusEntity;
import com.pcr.bot.service.GroupStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Codi
 * @date 2020/8/3
 **/
@Component
public class LockCommand {


    @Autowired
    private GroupStatusService groupStatusService;
    @Autowired
    GuildCommand guildCommand;


    /**
     *  处理锁定的命令
     */
    public void handlerLock(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        groupStatusService.lock(groupCode);
        String sendMsg = "锁定成功";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);

    }

    /**
     *  处理解锁的命令
     */
    public void handlerUnLock(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        groupStatusService.unLock(groupCode);
        String sendMsg = "解锁成功";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    public boolean isLock(String groupCode) {
        GroupStatusEntity groupStatusEntity = groupStatusService.getGroupStatusEntity(groupCode);
        if (groupStatusEntity == null) return false;
        if (groupStatusEntity.getStatus() == Constant.Lock.lock.getValue()) return true;
        return false;
    }
}
