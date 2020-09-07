package com.pcr.bot.command;

import com.forte.qqrobot.beans.messages.msgget.GroupMemberIncrease;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberReduce;
import com.pcr.bot.service.GuildPersonService;
import com.pcr.bot.service.KnifeService;
import com.pcr.bot.service.SubscribeBossService;
import com.pcr.bot.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author Codi
 * @date 2020/8/8
 **/
@Slf4j
@Component
public class GroupMemberChangeCommand {
    @Autowired
    GuildPersonService guildPersonService;
    @Autowired
    KnifeService knifeService;
    @Autowired
    TreeService treeService;
    @Autowired
    SubscribeBossService subscribeBossService;
    /**
     * 新加进群组成员
     */
    public void groupMemberIncrease(GroupMemberIncrease groupMemberIncrease) {
        String operatedQQ = groupMemberIncrease.getQQCode();
        String groupCode = groupMemberIncrease.getGroup();
        // 由于拼接的东西比较长，用java自带的MessageFormat对消息进行格式化，会比较直观
        final MessageFormat message = new MessageFormat("QQ:{0}加入了群{1}中");
        final String printMsg = message.format(new Object[]{operatedQQ, groupCode});
        // 红色显眼儿一点
        log.debug(printMsg);
    }

    /**
     * 有成员退群
     *
     * 退群时，需要删除当前QQ在该群组中当天的所有记录
     */
    public void groupMemberReduce(GroupMemberReduce groupMemberReduce) {
        final String qqCode = groupMemberReduce.getQQCode();
        final String groupCode = groupMemberReduce.getGroup();
        //从公会中移除该用户
        guildPersonService.removeGuildPerson(groupCode, qqCode);
        //如果有申请出刀的记录，需要移除申请出刀记录
        knifeService.removeKnife(groupCode, qqCode);
        //移除挂树记录和sl记录
        treeService.removeTree(groupCode, qqCode);
        //移除预约记录
        subscribeBossService.unSubscribe(groupCode, qqCode);

        final MessageFormat message = new MessageFormat("QQ:{0}从群{1}中离开了");
        final String printMsg = message.format(new Object[]{qqCode, groupCode});
        // 红色显眼儿一点
        log.debug(printMsg);
    }
}
