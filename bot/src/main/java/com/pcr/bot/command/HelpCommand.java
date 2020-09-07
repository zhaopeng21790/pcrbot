package com.pcr.bot.command;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import org.springframework.stereotype.Component;

/**
 * @author Codi
 * @date 2020/8/7
 **/
@Component
public class HelpCommand {

    public void handlerHelp(GroupMsg groupMsg, MsgSender sender) {
        String sendMsg = "帮助文档请参考在线文档：" + "\n"
                + "https://docs.qq.com/sheet/DTEl6RkZPc0hlRlpn?tab=BB08J2"
                + "如果您对会战Bot有新的想法和建议欢迎到在线收集的意见表反馈：" + "\n"
                + "https://docs.qq.com/sheet/DTHFPTWpGS3FTb3RT?tab=BB08J2";
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
    }
}
