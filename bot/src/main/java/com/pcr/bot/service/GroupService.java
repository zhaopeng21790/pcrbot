package com.pcr.bot.service;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import org.springframework.stereotype.Service;

/**
 * @author Codi
 * @date 2020/8/2
 **/

public interface GroupService {
    void handleCommand(GroupMsg groupMsg, MsgSender sender) throws Exception;
}
