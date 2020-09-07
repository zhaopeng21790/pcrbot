package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupInfo;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.models.QQModel;
import com.pcr.bot.service.GuildService;
import com.pcr.bot.service.QqGroupAdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Codi
 * @date 2020/8/17
 **/
@Component
public class AdminCommand {

    @Autowired
    CommandUtils commandUtils;
    @Autowired
    QqGroupAdmService qqGroupAdmService;
    @Autowired
    GuildService guildService;

    public void handlerAdd(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        final String qqCode = groupMsg.getCode();
        final String msg = groupMsg.getMsg();
        boolean admin = commandUtils.isAdmin(groupCode, qqCode);
        if (!admin) throw new RRException(Constant.Error.no_permissions);
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        cqCodeFromMsg.remove(0);
        List<String> qqList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("新增管理员如下：\n");
        for (CQCode cqCode : cqCodeFromMsg) {
            QQModel qqModel = new QQModel(cqCode);
            qqList.add(qqModel.getQqCode());
            sb.append(qqModel.getNickname()).append("\n");
        }
        boolean addAdmin = qqGroupAdmService.addAdmin(groupCode, qqList);
        String result = addAdmin ? "成功":"失败";
        sb.append("结果：").append(result);
        sender.SENDER.sendGroupMsg(groupCode, sb.toString());
    }

    public void handlerReduce(GroupMsg groupMsg, MsgSender sender) {
        final String groupCode = groupMsg.getGroupCode();
        final String qqCode = groupMsg.getCode();
        final String msg = groupMsg.getMsg();
        boolean admin = commandUtils.isAdmin(groupCode, qqCode);
        if (!admin) throw new RRException(Constant.Error.no_permissions);
        List<CQCode> cqCodeFromMsg = CQCodeUtil.build().getCQCodeFromMsg(msg);
        cqCodeFromMsg.remove(0);
        List<String> qqList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("移除管理员如下：\n");
        for (CQCode cqCode : cqCodeFromMsg) {
            QQModel qqModel = new QQModel(cqCode);
            qqList.add(qqModel.getQqCode());
            sb.append(qqModel.getNickname()).append("\n");
        }
        int adminRows = qqGroupAdmService.removeAdmin(groupCode, qqList);
        sb.append("移除成功：").append(adminRows).append("个");
        sender.SENDER.sendGroupMsg(groupCode, sb.toString());
    }


    public void flushAdmin(GroupMsg groupMsg, MsgSender sender) {
        final String qqCode = groupMsg.getCode();
        final String groupCode = groupMsg.getGroupCode();
        boolean superAdmin = commandUtils.isAdmin(groupCode, qqCode);
        if (!superAdmin) throw new RRException(Constant.Error.no_permissions);
        qqGroupAdmService.removeAdmin(groupCode);
        GroupInfo groupInfo = sender.getGroupInfoByCode(groupCode);
        Map<String, String> adminNickList = groupInfo.getAdminNickList();
        List<String> qqList = new ArrayList<String>(adminNickList.keySet());
        boolean result = qqGroupAdmService.addAdmin(groupCode, qqList);
        String sendMsg = result?"管理员刷新：成功":"管理员刷新：失败";
        sender.SENDER.sendGroupMsg(groupCode, sendMsg);
    }










}
