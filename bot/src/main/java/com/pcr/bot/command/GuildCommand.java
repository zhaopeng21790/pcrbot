package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupInfo;
import com.forte.qqrobot.beans.messages.result.GroupMemberList;
import com.forte.qqrobot.beans.messages.result.inner.GroupMember;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.sender.senderlist.SenderGetList;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.DateUtil;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.entity.GuildPersonEntity;
import com.pcr.bot.entity.QqGroupAdmEntity;
import com.pcr.bot.models.QQModel;
import com.pcr.bot.service.GuildPersonService;
import com.pcr.bot.service.GuildService;
import com.pcr.bot.service.QqGroupAdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Codi
 * @date 2020/8/3
 **/
@Component
public class GuildCommand {
    @Autowired
    private GuildService guildService;
    @Autowired
    private GuildPersonService guildPersonService;
    @Autowired
    private BossStatusCommand bossStatusCommand;
    @Autowired
    LockCommand lockCommand;
    @Autowired
    QqGroupAdmService qqGroupAdmService;

    /**
     *  处理创建公会的命令
     */
    public void handlerCreateGuld(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        String[] strings = msg.split(" ");
        if (strings.length < 2) throw new RRException(Constant.Error.no_guild_name);
        boolean isExist = guildService.isExist(groupCode);
        if (isExist) throw new RRException(Constant.Error.exist_guild);
        String guildName = strings[strings.length-1];

        GroupInfo groupInfo = sender.getGroupInfoByCode(groupCode);
        Map<String, String> adminNickList = groupInfo.getAdminNickList();
        Set<String> qqAdminList = adminNickList.keySet();

        List<QqGroupAdmEntity> admEntityList = new ArrayList<>();
        for (String qq : qqAdminList) {
            QqGroupAdmEntity admEntity = new QqGroupAdmEntity();
            admEntity.setGroupCode(groupCode);
            admEntity.setQqCode(qq);
            admEntityList.add(admEntity);
        }
        //添加管理员
        qqGroupAdmService.saveOrUpdateBatch(admEntityList);

        String firstStr = msg.substring(2, 3);
        boolean isJP = firstStr.equals("日");
        boolean isCN = "国陆Bb".contains(firstStr);
        int rate = Constant.Rate.tw.getValue();
        if (isJP) rate = Constant.Rate.jp.getValue();
        if (isCN) rate = Constant.Rate.cn.getValue();
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setName(guildName);
        guildEntity.setGroupCode(groupCode);
        guildEntity.setQqCode(code);
        guildEntity.setRate(rate);
        guildEntity.setExpiredTime(DateUtil.getAfterMonthOfDay());
        boolean result = guildService.save(guildEntity);
        String sendMsg = "公会创建结果：" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);

        bossStatusCommand.initBoss(groupCode, rate);
    }

    /**
     *  处理加入全部成员的命令
     */
    public void handlerJoinGuildAllPersion(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        SenderGetList senderGetList = sender.GETTER;
        GroupMemberList groupMemberList = senderGetList.getGroupMemberList(groupMsg);
        GroupMember[] list = groupMemberList.getList();
        List<GuildPersonEntity> personEntities = new ArrayList<>();
        for (GroupMember member : list) {
            GuildPersonEntity personEntity = new GuildPersonEntity();
            personEntity.setQqCode(member.getQQ());
            personEntity.setGroupCode(member.getGroup());
            personEntity.setName(member.getRemarkOrNickname());
            personEntities.add(personEntity);
        }
        boolean result = guildPersonService.saveUsers(personEntities);
        String sendMsg = "公会加入/更新全部成员：" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     *  处理加入公会@某人 的命令
     */
    public void handlerJoinGuildAtPersion(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        final String nickname = groupMsg.getRemarkOrNickname();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        List<QQModel> qqModels = convertToQQMember(msg);
        List<GuildPersonEntity> personEntities = new ArrayList<>();
        if (qqModels.size() == 0) {
            //加入自己
            GuildPersonEntity personEntity = new GuildPersonEntity();
            personEntity.setQqCode(code);
            personEntity.setGroupCode(groupCode);
            personEntity.setName(nickname);
            personEntities.add(personEntity);
        }
        for (QQModel qqModel : qqModels) {
            GuildPersonEntity personEntity = new GuildPersonEntity();
            personEntity.setQqCode(qqModel.getQqCode());
            personEntity.setGroupCode(groupCode);
            personEntity.setName(qqModel.getNickname());
            personEntities.add(personEntity);
        }
        boolean result = guildPersonService.saveUsers(personEntities);
        String sendMsg = "公会成员" + "加入/更新：" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }


    /**
     *  退出公会@某人的命令
     */
    public void handlerExitGuildAtPersion(GroupMsg groupMsg, MsgSender sender) throws Exception {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        final String nickname = groupMsg.getRemarkOrNickname();
        // 接收到消息的群号
        final String groupCode = groupMsg.getGroupCode();
        // 发消息人发的消息
        final String msg = groupMsg.getMsg();
        List<QQModel> qqModels = convertToQQMember(msg);
        if (qqModels.size() == 0) {
            throw new RRException("退会需要@指定人员");
        }
        List<GuildPersonEntity> personEntities = new ArrayList<>();
        String sendMsg = "";
        for (QQModel qqModel : qqModels) {
            GuildPersonEntity personEntity = new GuildPersonEntity();
            personEntity.setQqCode(qqModel.getQqCode());
            personEntity.setGroupCode(groupCode);
            personEntity.setName(qqModel.getNickname());
            personEntities.add(personEntity);
            sendMsg = sendMsg + qqModel.getNickname() + "\n";
        }
        boolean result = guildPersonService.removeGuildPersons(personEntities);
        sendMsg =  sendMsg + "\n退会" + (result ? "成功":"失败");
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }

    /**
     * 当前群组是否已经创建公会
     */
    public boolean isExistGuild(String groupCode) {
        return guildService.isExist(groupCode);
    }

    /**
     * 当前群组是否已经创建公会
     */
    public boolean isExpired(String groupCode) {
        return guildService.isExpired(groupCode);
    }


    public boolean isExistInGroupOfQQ(String groupCode, String qqCode) {
        return guildPersonService.isExist(groupCode, qqCode);
    }

    public GuildEntity getGuild(String groupCode) {
        return guildService.getGuild(groupCode);
    }




    /**
     * 在一条消息中@某人时，将msg中提取目标人物的信息
     */
    private List<QQModel> convertToQQMember(String msg) {
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        List<QQModel> list = new ArrayList<>();
        for (CQCode cqCode : cqCodeFromMsg) {
            QQModel qqModel = new QQModel(cqCode);
            list.add(qqModel);
        }
        return list;
    }
}
