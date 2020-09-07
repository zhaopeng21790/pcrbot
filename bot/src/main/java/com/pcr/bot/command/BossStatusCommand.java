package com.pcr.bot.command;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.StringUtils;
import com.pcr.bot.entity.BossSectionInfoEntity;
import com.pcr.bot.entity.BossStatusEntity;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.entity.KnifeEntity;
import com.pcr.bot.service.BossSectionInfoService;
import com.pcr.bot.service.BossStatusService;
import com.pcr.bot.service.KnifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Codi
 * @date 2020/8/3
 **/
@Component
public class BossStatusCommand {
    @Autowired
    BossStatusCommand bossStatusCommand;
    @Autowired
    BossSectionInfoService bossSectionInfoService;
    @Autowired
    BossStatusService bossStatusService;
    @Autowired
    LockCommand lockCommand;
    @Autowired GuildCommand guildCommand;
    @Autowired
    KnifeService knifeService;

    public void initBoss(String groupCode, Integer rate) {
        BossSectionInfoEntity bossSectionInfo = bossSectionInfoService.getBossSectionInfo(1, 1, rate);
        BossStatusEntity bossStatusEntity = new BossStatusEntity();
        String monthString = DateUtil.getCuttentDateOfMonthString();
        bossStatusEntity.setGuildDate(monthString);
        bossStatusEntity.setGroupCode(groupCode);
        bossStatusEntity.setWeekNum(bossSectionInfo.getSection());
        bossStatusEntity.setBossNum(bossSectionInfo.getBossNum());
        bossStatusEntity.setBossBlood(bossSectionInfo.getBossBlood());
        bossStatusEntity.setTotalBlood(bossSectionInfo.getBossBlood());
        bossStatusService.saveOrUpdate(bossStatusEntity);
    }

    public void handlerBossStatus(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        String sendMsg = "现在第"+ currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() +"号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        //获取挡墙的申请人
        KnifeEntity currentKnife = knifeService.getCurrentKnife(groupCode);
        if (currentKnife != null) {
            sendMsg = sendMsg + "\n" + currentKnife.getNickname() + "正在挑战boss";
        }
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 修改boss的周目
     */
    public void handlerEidtBossWeekNumStatus(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String numberText = StringUtils.getNumberText(msg);
        int parseInt = Integer.parseInt(numberText);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        currentBossStatus.setWeekNum(parseInt);
        bossStatusService.saveOrUpdate(currentBossStatus);
        String sendMsg = "现在第"+ currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() +"号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 修改boss的血量
     */
    public void handlerEidtBossBloodStatus(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String numberText = StringUtils.getNumberText(msg);
        Integer valueOf = Integer.valueOf(numberText);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        currentBossStatus.setBossBlood(valueOf);
        bossStatusService.saveOrUpdate(currentBossStatus);
        String sendMsg = "现在第"+ currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() +"号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 修改boss的序号
     */
    public void handlerEidtBossNumStatus(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String numberText = StringUtils.getNumberText(msg);
        Integer valueOf = Integer.valueOf(numberText);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        currentBossStatus.setBossNum(valueOf);
        bossStatusService.saveOrUpdate(currentBossStatus);
        String sendMsg = "现在第"+ currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() +"号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 修改boss的序号
     */
    public void handlerEidtBossInit(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        GuildEntity guild = guildCommand.getGuild(groupCode);
        initBoss(groupCode, guild.getRate());
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        String sendMsg = "现在第"+ currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() +"号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }



}
