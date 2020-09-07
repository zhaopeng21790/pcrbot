package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.entity.*;
import com.pcr.bot.models.QQModel;
import com.pcr.bot.service.BossSectionInfoService;
import com.pcr.bot.service.BossStatusService;
import com.pcr.bot.service.KnifeHistoryService;
import com.pcr.bot.service.KnifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Codi
 * @date 2020/8/3
 **/
@Component
public class KnifeCommand {
    @Autowired
    KnifeService knifeService;
    @Autowired
    BossStatusService bossStatusService;
    @Autowired
    KnifeHistoryService knifeHistoryService;
    @Autowired
    BossSectionInfoService bossSectionInfoService;
    @Autowired
    SubscribeCommand subscribeCommand;
    @Autowired
    CheckCommand checkCommand;
    @Autowired
    LockCommand lockCommand;
    @Autowired
    GuildCommand guildCommand;
    @Autowired
    CommandUtils commandUtils;


    /**
     * 申请出刀
     */
    public void handlerApplyKnife(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        final String nickname = groupMsg.getRemarkOrNickname();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        //检查是否已经申请
        KnifeEntity currentKnife = knifeService.getCurrentKnife(groupCode);
        if (currentKnife != null) throw new RRException("申请失败，"+currentKnife.getNickname() + "正在挑战boss");
        KnifeHistoryEntity knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, code);
        if (knifeHistoryEntity != null && knifeHistoryEntity.getIsRewardKnife() == 0 && knifeHistoryEntity.getSeqId() == 3) throw new RRException(Constant.Error.knife_out);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        KnifeEntity knifeEntity = new KnifeEntity();
        knifeEntity.setGroupCode(groupCode);
        knifeEntity.setQqCode(code);
        knifeEntity.setNickname(nickname);
        knifeEntity.setBossNum(currentBossStatus.getBossNum());
        knifeService.save(knifeEntity);
        String sendMsg = nickname + "已开始挑战boss" + "\n"
                + "现在第" + currentBossStatus.getWeekNum() + "周目，"
                + currentBossStatus.getBossNum() + "号boss" + "\n"
                + "生命值: "  + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);

    }

    /**
     * 取消出刀
     */
    public void handlerCancelKnife(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        knifeService.removeKnife(groupCode);
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), "取消成功");
    }

    /**
     * 撤销报刀
     */
    public void handlerUndoKnife(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        final String code = groupMsg.getCode();
        KnifeHistoryEntity knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, null);
        if (knifeHistoryEntity == null) {
            throw new RRException("本群今日还没有报刀记录");
        }
        boolean admin = commandUtils.isAdmin(groupCode, code);
        boolean isMe = knifeHistoryEntity.getQqCode().equals(code);
        if (!isMe && !admin) {
            String sendMsg = "最后一刀为" + knifeHistoryEntity.getNickname() + "的报刀，您没有权限取消他人的报刀，请联系管理员或者本人";
            throw new RRException(sendMsg);
        }
        //移除报刀
        knifeHistoryService.removeById(knifeHistoryEntity.getId());
        if (knifeHistoryEntity.getIsKilled() == 1) {
            GuildEntity guildEntity = guildCommand.getGuild(groupCode);
            int section = computeSection(knifeHistoryEntity.getWeekNum(), guildEntity.getRate());
            BossSectionInfoEntity bossSectionInfo = bossSectionInfoService.getBossSectionInfo(section, knifeHistoryEntity.getBossNum(), guildEntity.getRate());
            //修改boss血量
            BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
            currentBossStatus.setBossBlood(knifeHistoryEntity.getDamage());
            currentBossStatus.setBossNum(knifeHistoryEntity.getBossNum());
            currentBossStatus.setWeekNum(knifeHistoryEntity.getWeekNum());
            if (bossSectionInfo != null) {
                currentBossStatus.setTotalBlood(bossSectionInfo.getBossBlood());
            }
            bossStatusService.saveOrUpdate(currentBossStatus);
        }else {
            //修改boss血量
            BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
            int newBlood = currentBossStatus.getBossBlood() + knifeHistoryEntity.getDamage();
            currentBossStatus.setBossBlood(newBlood);
            bossStatusService.saveOrUpdate(currentBossStatus);
        }
        String sendMsg = knifeHistoryEntity.getNickname() + "：出刀记录已被撤销";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 报刀20000@某人
     */
    public void handlerReportKnife(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        // 发消息人的群昵称或者昵称
        String nickname = groupMsg.getRemarkOrNickname();

        //移除cq码的报刀字符串
        String msg1 = CQCodeUtil.build().removeCQCodeFromMsg(msg).trim().toLowerCase();
        String damageMsg = msg1.replace("报刀", "");
        damageMsg = damageMsg.replace(" ","");
        Integer damage = convertDamage(damageMsg);
        CQCode cqCode = null;
        try {
            cqCode = CQCodeUtil.build().toCQCode(msg.replace(msg1, ""));
        } catch (Exception e) {
            //无代报刀
        }
        boolean isKilledBoss = false;
        KnifeHistoryEntity knifeHistoryEntity = null;
        if (cqCode != null) {
            QQModel qqModel = new QQModel(cqCode);
            nickname = qqModel.getNickname();
            isKilledBoss = reportDamage(groupCode, qqModel.getQqCode(), nickname, damage);
            knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, qqModel.getQqCode());

        }else {
            isKilledBoss = reportDamage(groupCode, code, nickname,damage);
            knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, code);
        }
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);

        if (knifeHistoryEntity == null) {
            knifeHistoryEntity = new KnifeHistoryEntity();
            knifeHistoryEntity.setStatus(0);
            knifeHistoryEntity.setSeqId(1);
        }
        String dsg = knifeHistoryEntity.getStatus() == 0 ? "完整刀" : "补偿刀";
        String sendMsg = nickname + "对boss造成了" + damage + "点伤害" + "\n"
                + "今日第" + knifeHistoryEntity.getSeqId() + "刀，" + dsg + "\n"
                + "现在第" + currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() + "号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);

        try {
            if (isKilledBoss) {
                //通知预约boss的人员，
                subscribeCommand.notifySubscriber(groupCode, currentBossStatus.getBossNum());
                //通知挂树的人员下树
                checkCommand.clearTreeOfDay(groupCode, DateUtil.getPCRDateOfDayString());
            }
        } catch (Exception e) {
            //通知异常无所谓
        }

    }

    /**
     * 尾刀
     */
    public void handlerKillBoss(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        // 发消息人的群昵称或者昵称
        String nickname = groupMsg.getRemarkOrNickname();

        //移除cq码的报刀字符串
        String msg1 = CQCodeUtil.build().removeCQCodeFromMsg(msg).trim().toLowerCase();
        BossStatusEntity oldCurrentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        Integer damage = oldCurrentBossStatus.getBossBlood();
        CQCode cqCode = null;
        KnifeHistoryEntity knifeHistoryEntity = null;
        try {
            cqCode = CQCodeUtil.build().toCQCode(msg.replace(msg1, ""));
        } catch (Exception e) {
            //无代报刀
        }
        boolean isKilledBoss = false;
        if (cqCode != null) {
            QQModel qqModel = new QQModel(cqCode);
            nickname = qqModel.getNickname();
            isKilledBoss = reportDamage(groupCode, qqModel.getQqCode(), nickname,damage);
            knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, qqModel.getQqCode());
        }else {
            isKilledBoss = reportDamage(groupCode, code, nickname,damage);
            knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, code);
        }
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        if (knifeHistoryEntity == null) {
            knifeHistoryEntity = new KnifeHistoryEntity();
            knifeHistoryEntity.setSeqId(1);
            knifeHistoryEntity.setStatus(0);
        }

        String s = knifeHistoryEntity.getStatus() == 0 ? "收尾刀" : "补偿刀";
        String sendMsg = nickname + "对boss造成了" + damage + "点伤害，击杀了boss" + "\n"
                + "今日第" + knifeHistoryEntity.getSeqId() + "刀，" + s + "\n"
                + "现在第" + currentBossStatus.getWeekNum() + "周目，" + currentBossStatus.getBossNum() + "号boss" + "\n"
                + "生命值：" + currentBossStatus.getBossBlood();
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);

        try {
            if (isKilledBoss) {
                //通知预约boss的人员，
                subscribeCommand.notifySubscriber(groupCode, currentBossStatus.getBossNum());
                //通知挂树的人员下树
                checkCommand.clearTreeOfDay(groupCode, DateUtil.getPCRDateOfDayString());
            }
        } catch (Exception e) {
            //通知异常无所谓
        }
    }

    /**
     * 上报伤害
     * @param groupCode 群号
     * @param qqCode    qq号
     * @param name      qq名称
     * @param damage    伤害值
     */
    public boolean reportDamage(String groupCode, String qqCode, String name, Integer damage) {
        commandUtils.checkCodeInGuild(groupCode, qqCode);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        int newBlood = currentBossStatus.getBossBlood() - damage;
        if (newBlood < 0) throw new RRException(Constant.Error.boss_blood_exception);
        //当天最后一次的出刀记录
        KnifeHistoryEntity knifeHistoryEntity = knifeHistoryService.lastKnifeHistory(groupCode, qqCode);
        if (knifeHistoryEntity != null && knifeHistoryEntity.getIsRewardKnife() == 0 && knifeHistoryEntity.getSeqId() == 3) {
            String msg = name + "今日出刀次数已用完，无法报刀";
            throw new RRException(msg);
        }
        KnifeHistoryEntity newKnifeHistoryEntity = new KnifeHistoryEntity();
        newKnifeHistoryEntity.setGroupCode(groupCode);
        newKnifeHistoryEntity.setQqCode(qqCode);
        newKnifeHistoryEntity.setDamage(damage);
        newKnifeHistoryEntity.setNickname(name);
        newKnifeHistoryEntity.setWeekNum(currentBossStatus.getWeekNum());
        newKnifeHistoryEntity.setDay(DateUtil.getPCRDateOfDayString());
        newKnifeHistoryEntity.setBossNum(currentBossStatus.getBossNum());
        int hasReward = newBlood == 0 ? 1 : 0;
        if (knifeHistoryEntity == null) {
            //说明之前没有报刀记录，为第一刀完整刀
            newKnifeHistoryEntity.setStatus(Constant.Knife.full.getValue());
            newKnifeHistoryEntity.setSeqId(1);
            newKnifeHistoryEntity.setIsRewardKnife(hasReward);
        }else {
            //如果上次出刀产生了补偿时间，则本次为补偿刀
            if (knifeHistoryEntity.getIsRewardKnife() == 1) {
                newKnifeHistoryEntity.setIsRewardKnife(0);
                newKnifeHistoryEntity.setStatus(Constant.Knife.reward.getValue());
                newKnifeHistoryEntity.setSeqId(knifeHistoryEntity.getSeqId());
            }else {
                newKnifeHistoryEntity.setIsRewardKnife(hasReward);
                newKnifeHistoryEntity.setStatus(Constant.Knife.full.getValue());
                newKnifeHistoryEntity.setSeqId(knifeHistoryEntity.getSeqId() + 1);
            }
        }
        if (newBlood == 0) {
            newKnifeHistoryEntity.setIsKilled(1);
        }else {
            newKnifeHistoryEntity.setIsKilled(0);
        }
        //向记录中添加一个出刀记录
        knifeHistoryService.save(newKnifeHistoryEntity);
        //删除申请出刀的状态
        knifeService.removeKnife(groupCode, qqCode);
        //把当前用户移除当前boss的预约
        try {
            subscribeCommand.unSubscribeBoss(groupCode, qqCode, currentBossStatus.getBossNum());
        } catch (Exception e) {
            //没有预约无所谓
        }

        //更改当前群组中boss的状态
        if (newBlood == 0) {//击杀了boss
            killBoss(groupCode, qqCode);
            return true;
        }else {
            currentBossStatus.setBossBlood(newBlood);
            bossStatusService.saveOrUpdate(currentBossStatus);
            return false;
        }
    }

    /**
     * 计算第几阶段
     * @param weekNum 当前所在的周目
     * @param rate    公会类型/台/日/国
     */
    private int computeSection(int weekNum, int rate) {
        int section = 1;
        if (rate == Constant.Rate.cn.getValue()) {
            //国服
            if (weekNum > 1) {
                section = 2;
            }
        }else {
            if (weekNum > 34) {
                section = 4;
            }else if (weekNum > 10) {
                section = 3;
            }else if (weekNum > 3) {
                section = 2;
            }
        }
        return section;
    }


    /**
     * 击杀boss
     */
    private void killBoss(String groupCode, String qqCode) {
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        int bossNum = currentBossStatus.getBossNum();
        int weekNum = currentBossStatus.getWeekNum();
        if (bossNum == 5) {
            weekNum = weekNum + 1;
        }
        int newBossNum = bossNum + 1;
        if (newBossNum > 5) {
            newBossNum = 1;
        }
        GuildEntity guildEntity = guildCommand.getGuild(groupCode);
        int section = computeSection(weekNum, guildEntity.getRate());
        //获取对应全新boss的信息
        BossSectionInfoEntity bossSectionInfo = bossSectionInfoService.getBossSectionInfo(section, newBossNum, guildEntity.getRate());
        currentBossStatus.setBossBlood(bossSectionInfo.getBossBlood());
        currentBossStatus.setBossNum(bossSectionInfo.getBossNum());
        currentBossStatus.setWeekNum(weekNum);
        currentBossStatus.setTotalBlood(bossSectionInfo.getBossBlood());
        bossStatusService.saveOrUpdate(currentBossStatus);
    }

    public boolean isApplyKnife(String groupCode, String qqCode) {
        return knifeService.isExists(groupCode, qqCode);
    }

    /**
     * 将报刀200万/200w/200W/2000000转化为2000000
     */
    private Integer convertDamage(String msg) {
        if (msg.endsWith("万")) {
            msg = msg.replace("万","");
            return Integer.parseInt(msg) * 10000;
        }else if (msg.endsWith("w")) {
            msg = msg.replace("w","");
            return Integer.parseInt(msg) * 10000;
        }else if (msg.endsWith("W")) {
            msg = msg.replace("W","");
            return Integer.parseInt(msg) * 10000;
        }
        return Integer.valueOf(msg);
    }
}
