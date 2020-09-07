package com.pcr.bot.service.impl;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.pcr.bot.command.CommandDispatch;
import com.pcr.bot.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Codi
 * @date 2020/8/2
 **/

@Slf4j
@Service("groupService")
public class GroupServiceImpl implements GroupService {

    @Autowired
    CommandDispatch commandDispatch;

    @Override
    public void handleCommand(GroupMsg groupMsg, MsgSender sender) throws Exception {
        commandDispatch.handleCommand(groupMsg, sender);
    }
}
