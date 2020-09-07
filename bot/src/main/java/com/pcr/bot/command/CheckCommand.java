package com.pcr.bot.command;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberList;
import com.forte.qqrobot.beans.messages.result.inner.GroupMember;
import com.forte.qqrobot.bot.BotManager;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.sender.senderlist.SenderGetList;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.common.utils.RestTemplateUtil;
import com.pcr.bot.entity.BossStatusEntity;
import com.pcr.bot.entity.GuildPersonEntity;
import com.pcr.bot.entity.KnifeHistoryEntity;
import com.pcr.bot.entity.TreeEntity;
import com.pcr.bot.models.QQModel;
import com.pcr.bot.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Codi
 * @date 2020/8/3
 **/
@Component
public class CheckCommand {
    @Autowired
    BossStatusService bossStatusService;
    @Autowired
    TreeService treeService;
    @Autowired
    BotManager botManager;
    @Autowired
    LockCommand lockCommand;
    @Autowired
    GuildCommand guildCommand;
    @Autowired
    KnifeHistoryService knifeHistoryService;
    @Autowired
    KnifeService knifeService;
    @Autowired
    CommandUtils commandUtils;
    @Autowired
    GuildPersonService guildPersonService;


    /**
     * 催刀功能
     */
    public void handlerTipKnife(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        String ofDayString = DateUtil.getPCRDateOfDayString();
        //查询一个群组出刀下班人员
        List<KnifeHistoryEntity> entityList = knifeHistoryService.getKnifeHistoryOffDuty(groupCode, ofDayString);
        List<String> qqList = entityList.stream().map(KnifeHistoryEntity::getQqCode).collect(Collectors.toList());

        //获取当前在群组中的人员，防止有人退群导致@消息的失败
        List<String> inGroupQQList = commandUtils.getAllQQFromGroupCode(groupCode);

        //查处当前群组的总人数
        List<GuildPersonEntity> allPersonInGroup = guildPersonService.getAllPersonInGroup(groupCode);

        //保留仅在群组中的人员
        List<GuildPersonEntity> inGroupAllPersons = allPersonInGroup.stream().filter(e -> inGroupQQList.contains(e.getQqCode())).collect(Collectors.toList());

        //list中保存了当前群组中未出刀完成的人员
        List<GuildPersonEntity> list = inGroupAllPersons.stream().filter(e -> !qqList.contains(e.getQqCode())).collect(Collectors.toList());
        String sendMsg = "可可罗温馨提示，请一下同学及时完成今日出刀\n";
        CQCodeUtil build = CQCodeUtil.build();
        for (GuildPersonEntity personEntity : list) {
            sendMsg = sendMsg + build.getCQCode_At(personEntity.getQqCode()) + "\n";
        }
        sender.SENDER.sendGroupMsg(groupCode, sendMsg.trim());
    }


    /**
     * 查刀/查刀@群友
     */
    public void handlerCheckCurrentDayReport(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String ofDayString = DateUtil.getPCRDateOfDayString();
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        if (cqCodeFromMsg != null && cqCodeFromMsg.size() > 0) {
            //查刀@群友
            CQCode cqCode = cqCodeFromMsg.get(0);
            QQModel qqModel = new QQModel(cqCode);
            commandUtils.checkCodeInGuild(groupCode, qqModel.getQqCode());
            //查刀
            String sendMsg = qqModel.getNickname() + getPersonKnifeOfDay(groupCode, qqModel.getQqCode(), ofDayString);
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
        }else {
            //查刀
            String sendMsg = getKnifeOfDay(groupCode, ofDayString);
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
        }

    }

    /**
     * 获取昨天的出刀
     */
    public void handlerCheckYesterdayDayReport(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        String ofDayString = DateUtil.getPCRDateOfYesterdayString();
        String sendMsg = getKnifeOfDay(groupCode, ofDayString);
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 获取指定日期的出刀
     */
    public void handlerCheckAnyDayReport(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String day = msg.replace("报告", "");
        String sendMsg = getKnifeOfDay(groupCode, day);
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 查SL
     */
    public void handlerCheckSL(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String code = groupMsg.getCode();
        final String msg = groupMsg.getMsg();
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        List<TreeEntity> list = null;
        if (cqCodeFromMsg.size() > 0) {
            QQModel qqModel = new QQModel(cqCodeFromMsg.get(0));
            //查某人的sl记录
            list = treeService.getList(groupCode, qqModel.getQqCode(), Constant.Type.SL.getValue());
        }else {
            //查询当前群组的sl记录
            list = treeService.getList(groupCode, Constant.Type.SL.getValue());
        }
        if (list == null || list.size() == 0) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(),"当天暂无SL记录");
            return;
        }
        StringBuilder sendMsg = new StringBuilder("当天SL记录如下：\n");
        for (TreeEntity treeEntity : list) {
            sendMsg.append(treeEntity.getNickname()).append("：").append(DateUtil.dateToString(treeEntity.getCreateTime()));
            if (StringUtils.isNotBlank(treeEntity.getMessage())) {
                sendMsg.append(" ：").append(treeEntity.getMessage());
            }
            sendMsg.append("\n");
        }
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg.toString().trim());
    }

    /**
     * SL
     */
    public void handlerSL(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        String msg = groupMsg.getMsg();
        String code = groupMsg.getCode();
        String nickname = groupMsg.getRemarkOrNickname();
        TreeEntity treeEntity = getTreeEntity(groupMsg);
        treeEntity.setType(1);
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        if (cqCodeFromMsg.size() > 0) {
            QQModel qqModel = new QQModel(cqCodeFromMsg.get(0));
            nickname = qqModel.getNickname();
            code = qqModel.getQqCode();
            treeEntity.setQqCode(qqModel.getQqCode());
            treeEntity.setNickname(nickname);
            msg = CQCodeUtil.build().removeCQCodeFromMsg(msg);
        }
        String[] strings = msg.split(" ");
        if (strings.length > 1) {
            treeEntity.setMessage(strings[1]);
        }
        commandUtils.checkCodeInGuild(groupCode, code, nickname);
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        treeEntity.setBossNum(currentBossStatus.getBossNum());
        treeService.addTreeEntity(treeEntity);
        CQCode cqCode_at = CQCodeUtil.build().getCQCode_At(code);
        String sendMsg = cqCode_at.toString() + "已记录SL";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
        //删除申请出刀的状态
        knifeService.removeKnife(groupCode, code);
    }


    /**
     * 取消SL@群友
     */
    public void handlerCandelSL(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String code = groupMsg.getCode();
        String nickname = groupMsg.getRemarkOrNickname();
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        if (cqCodeFromMsg.size() > 0) {
            //说明有@的操作
            boolean admin = commandUtils.isAdmin(groupCode, code);
            if (!admin) throw new RRException(Constant.Error.no_permissions);
            CQCode cqCode = cqCodeFromMsg.get(0);
            QQModel model = new QQModel(cqCode);
            code = model.getQqCode();
            nickname = model.getNickname();
        }
        commandUtils.checkCodeInGuild(groupCode, code, nickname);
        boolean result = treeService.removeTree(groupCode, code, Constant.Type.SL.getValue());
        //删除群组中指定人员的申请出刀的记录
        if (result) knifeService.removeKnife(groupCode, code);
        String sendMsg = nickname + "取消SL：" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupCode, sendMsg);
    }

    /**
     * 挂树
     */
    public void handlerLinkTree(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String code = groupMsg.getCode();
        TreeEntity treeEntity = getTreeEntity(groupMsg);
        treeEntity.setType(Constant.Type.TREE.getValue());
        BossStatusEntity currentBossStatus = bossStatusService.getCurrentBossStatus(groupCode);
        treeEntity.setBossNum(currentBossStatus.getBossNum());
        treeService.addTreeEntity(treeEntity);
        //删除群组中指定人员的申请出刀的记录
        knifeService.removeKnife(groupCode, code);
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), "已记录挂树");
    }


    /**
     * 下树/下树@某人
     */
    public void handlerUnLinkTree(GroupMsg groupMsg, MsgSender sender) {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        String nickname = groupMsg.getRemarkOrNickname();
        String code = groupMsg.getCode();
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        if (cqCodeFromMsg.size() > 0) {
            boolean admin = commandUtils.isAdmin(groupCode, code);
            if (!admin) throw new RRException(Constant.Error.no_permissions);
            CQCode cqCode = cqCodeFromMsg.get(0);
            QQModel model = new QQModel(cqCode);
            code = model.getQqCode();
            nickname = model.getNickname();
        }
        commandUtils.checkCodeInGuild(groupCode, code);
        boolean result = treeService.removeTree(groupCode, code, Constant.Type.TREE.getValue());
        //删除群组中指定人员的申请出刀的记录
        if (result) knifeService.removeKnife(groupCode, code);
        String sendMsg = nickname + "下树" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupCode, sendMsg);
    }


    /**
     * 查树
     */
    public void handlerCheckTree(GroupMsg groupMsg, MsgSender sender)  {
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        List<TreeEntity> list = treeService.getList(groupCode, 0);
        if (list == null || list.size() == 0) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), "暂无挂树成员");
            return;
        }
        String sendMsg = "挂树的成员：\n";
        for (TreeEntity entity : list) {
            sendMsg = sendMsg + entity.getNickname() + "\n";
        }
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg.trim());
    }


    public void handlerWaKuang(GroupMsg groupMsg, MsgSender sender) {
        String msg = groupMsg.getMsg();
        String url = Constant.URL.pcr_python_host.getValue() + "/miner?rank=" + msg;
        JSONObject jsonObject = RestTemplateUtil.getForm(url, JSONObject.class);
        String errorMessage = jsonObject.getString("message");
        if (StringUtils.isNotBlank(errorMessage)) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), errorMessage.trim());
            return;
        }
        String data = jsonObject.getString("data");
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), data.trim());

    }


    public void clearTreeOfDay(String groupCode, String day) {
        List<TreeEntity> list = treeService.getList(groupCode, 0);
        if (list == null || list.size() == 0) {
            return;
        }
        String sendMsg = "boss已被击败，可以下树了：\n";
        CQCodeUtil build = CQCodeUtil.build();
        for (TreeEntity entity : list) {
            CQCode cqCode_at = build.getCQCode_At(entity.getQqCode());
            sendMsg  = sendMsg + cqCode_at + "\n";
        }
        treeService.clearTreeOfDay(groupCode, day);
        MsgSender sender = botManager.defaultBot().getSender();
        sender.SENDER.sendGroupMsg(groupCode, sendMsg.trim());
    }

    private TreeEntity getTreeEntity(GroupMsg groupMsg) {
        String code = groupMsg.getCode();
        String remarkOrNickname = groupMsg.getRemarkOrNickname();
        String groupCode = groupMsg.getGroupCode();
        TreeEntity treeEntity = new TreeEntity();
        treeEntity.setGroupCode(groupCode);
        treeEntity.setQqCode(code);
        treeEntity.setNickname(remarkOrNickname);
        treeEntity.setDay(DateUtil.getPCRDateOfDayString());
        treeEntity.setCreateTime(new Date());
        return treeEntity;
    }

    private String getPersonKnifeOfDay(String groupCode, String qqCode,String day) {
        List<KnifeHistoryEntity> entityList = knifeHistoryService.getKnifeHistory(groupCode, qqCode, day);
        //已经出了多少完整刀
        List<KnifeHistoryEntity> completeList = entityList.stream().filter(e -> e.getStatus() == 0).collect(Collectors.toList());
        //目前共产生了多少补偿刀
        List<KnifeHistoryEntity> rewardList = entityList.stream().filter(e -> e.getIsRewardKnife() == 1).collect(Collectors.toList());
        //目前已经出了多少补偿刀
        List<KnifeHistoryEntity> usedRewardList = entityList.stream().filter(e -> e.getStatus() == 1).collect(Collectors.toList());

        long totalDamage = 0;
        StringBuilder sendMsg = new StringBuilder(day + "出刀概括：\n");
        for (KnifeHistoryEntity knifeHistoryEntity : entityList) {
            totalDamage += knifeHistoryEntity.getDamage();
            String knifeTypeStr = knifeHistoryEntity.getStatus() == Constant.Knife.full.getValue() ? "完整刀" : "补偿刀";
            sendMsg.append("第")
                    .append(knifeHistoryEntity.getSeqId())
                    .append("刀,")
                    .append(knifeTypeStr)
                    .append("：对")
                    .append(knifeHistoryEntity.getBossNum())
                    .append("王造成了")
                    .append(knifeHistoryEntity.getDamage())
                    .append("伤害\n");
        }
        sendMsg.append("总计对boss造成伤害：").append(totalDamage).append("\n\n");
        sendMsg.append("已出的完整刀：").append(completeList.size()).append("刀\n");
        sendMsg.append("已出的补偿刀：").append(usedRewardList.size()).append("刀\n");
        sendMsg.append("未出的补偿刀：").append((rewardList.size() - usedRewardList.size())).append("刀\n");
        sendMsg.append("未出的完整刀：").append((3 - completeList.size())).append("刀");
        return sendMsg.toString();
    }

    private String getKnifeOfDay(String groupCode, String day) {
        List<KnifeHistoryEntity> entityList = knifeHistoryService.getKnifeHistory(groupCode, day);
        //已经出了多少完整刀
        List<KnifeHistoryEntity> completeList = entityList.stream().filter(e -> e.getStatus() == 0).collect(Collectors.toList());
        //目前共产生了多少补偿刀
        List<KnifeHistoryEntity> rewardList = entityList.stream().filter(e -> e.getIsRewardKnife() == 1).collect(Collectors.toList());
        //目前已经出了多少补偿刀
        List<KnifeHistoryEntity> usedRewardList = entityList.stream().filter(e -> e.getStatus() == 1).collect(Collectors.toList());
        String sendMsg = "本群" + day + "出刀情况：\n"
                + "已出的完整刀：" + completeList.size() + "刀\n"
                + "已出的补偿刀：" + usedRewardList.size() + "刀\n"
                + "未出的补偿刀：" + (rewardList.size() - usedRewardList.size()) + "刀\n"
                + "未出的完整刀：" + (90 - completeList.size()) + "刀";
        return sendMsg;
    }


}
