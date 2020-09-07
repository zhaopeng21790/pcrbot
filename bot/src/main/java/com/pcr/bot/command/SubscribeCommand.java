package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.bot.BotManager;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.StringUtils;
import com.pcr.bot.entity.SubscribeBossEntity;
import com.pcr.bot.service.SubscribeBossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Codi
 * @date 2020/8/3
 **/

@Component
public class SubscribeCommand {
    @Autowired
    BotManager botManager;
    @Autowired
    SubscribeBossService subscribeBossService;
    @Autowired
    LockCommand lockCommand;
    @Autowired
    GuildCommand guildCommand;
    /**
     *  处理预约boss的命令
     */
    public void handlerSubscribeBoss(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        final String nickname = groupMsg.getRemarkOrNickname();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        String numStr = StringUtils.getNumberText(msg);
        Integer bossNum = Integer.valueOf(numStr);
        if (bossNum<1 || bossNum > 5) throw new RRException(Constant.Error.boss_num_exception);
        boolean subscribed = subscribeBossService.isSubscribed(groupCode, code, bossNum);
        if (subscribed) throw new RRException(Constant.Error.is_subscribed);
        String[] strings = msg.split(" ");
        String dateStr = DateUtil.getPCRDateOfDayString();
        SubscribeBossEntity subscribeBossEntity = new SubscribeBossEntity();
        subscribeBossEntity.setBossNum(bossNum);
        subscribeBossEntity.setQqCode(code);
        subscribeBossEntity.setNickname(nickname);
        subscribeBossEntity.setGroupCode(groupCode);
        subscribeBossEntity.setSubscribeTime(dateStr);
        if (strings.length > 1) {
            subscribeBossEntity.setMessage(strings[1]);
        }
        subscribeBossService.save(subscribeBossEntity);
        String sendMsg = "预约成功";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     *  处理取消预约boss的命令
     */
    public void handlerUnSubscribeBoss(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        String numStr = StringUtils.getNumberText(msg);
        Integer bossNum = Integer.valueOf(numStr);
        unSubscribeBoss(groupCode, code, bossNum);
        String sendMsg = "取消成功";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 查询预约boss的信息
     */
    public void handlerSubscribeBossStatus(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        String numStr = StringUtils.getNumberText(msg);
        Integer bossNum = Integer.valueOf(numStr);
        List<SubscribeBossEntity> subscribedList = subscribeBossService.getSubscribed(groupCode, bossNum);
        if (subscribedList == null || subscribedList.size() == 0) throw new RRException("暂无人员预约" + bossNum + "号boss");
        String person = "";
        for (SubscribeBossEntity entity : subscribedList) {
            person = person + entity.getNickname();
            if (org.apache.commons.lang.StringUtils.isNotBlank(entity.getMessage())) {
                person = person + " ：" + entity.getMessage();
            }
            person = person + "\n";
        }
        String sendMsg = "预约"+bossNum+"号的成员:" + "\n" + "\n" + person.trim();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 查询预约boss的信息
     */
    public void handlerAllSubscribeBossStatus(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        List<SubscribeBossEntity> subscribedList = subscribeBossService.getSubscribed(groupCode);
        List<SubscribeBossEntity> list1 = subscribedList.stream().filter(s -> s.getBossNum() == 1).collect(Collectors.toList());
        List<SubscribeBossEntity> list2 = subscribedList.stream().filter(s -> s.getBossNum() == 2).collect(Collectors.toList());
        List<SubscribeBossEntity> list3 = subscribedList.stream().filter(s -> s.getBossNum() == 3).collect(Collectors.toList());
        List<SubscribeBossEntity> list4 = subscribedList.stream().filter(s -> s.getBossNum() == 4).collect(Collectors.toList());
        List<SubscribeBossEntity> list5 = subscribedList.stream().filter(s -> s.getBossNum() == 5).collect(Collectors.toList());
        String sendMsg = "当前boss预约情况：\n"
                + "1王预约人数：" + list1.size() + "\n"
                + "2王预约人数：" + list2.size() + "\n"
                + "3王预约人数：" + list3.size() + "\n"
                + "4王预约人数：" + list4.size() + "\n"
                + "5王预约人数：" + list5.size();

        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 取消订阅
     */
    public boolean unSubscribeBoss(String groupCode, String qqCode, Integer bossNum) {
        if (bossNum<1 || bossNum > 5) throw new RRException(Constant.Error.boss_num_exception);
        boolean subscribed = subscribeBossService.isSubscribed(groupCode, qqCode, bossNum);
        if (!subscribed) throw new RRException(Constant.Error.not_subscribed);
        return subscribeBossService.unSubscribe(groupCode, qqCode, bossNum);
    }


    public void notifySubscriber(String groupCode, Integer bossNum) {
        List<SubscribeBossEntity> subscribedList = subscribeBossService.getSubscribed(groupCode, bossNum);
        if (subscribedList == null || subscribedList.size() == 0) return;
        MsgSender sender = botManager.defaultBot().getSender();
        String sendMsg = "boss已被击败" + "\n";
        CQCodeUtil build = CQCodeUtil.build();
        for (SubscribeBossEntity entity : subscribedList) {
            CQCode cqCode_at = build.getCQCode_At(entity.getQqCode());
            sendMsg  = sendMsg + cqCode_at + "\n";
        }
        sender.SENDER.sendGroupMsg(groupCode, sendMsg.trim());
    }

}
