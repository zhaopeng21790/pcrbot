package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.springframework.stereotype.Component;

/**
 * @author Codi
 * @date 2020/8/6
 **/
@Component
public class RankCommand {
    public void handlerRankRecommend(GroupMsg groupMsg, MsgSender sender) {
        // 发消息人的账号
        final String code = groupMsg.getCode();
        final String nickname = groupMsg.getRemarkOrNickname();
        final String groupCode = groupMsg.getGroupCode();
        final String msg = groupMsg.getMsg();
        //^(\*?([日台国陆b])服?([前中后]*)卫?)?rank(表|推荐|指南)?$
        String firstStr = msg.substring(0, 1);
        String newMsg = msg.replace("服", "");
        boolean isTW = firstStr.equals("台");
        boolean isJP = firstStr.equals("日");
        boolean isCN = "国陆Bb".contains(firstStr);
        String secondStr = newMsg.substring(1, 2);
        boolean isQian = secondStr.equals("前");
        boolean isZhong = secondStr.equals("中");
        boolean isHou = secondStr.equals("后");
        String sendMsg = CQCodeUtil.build().getCQCode_At(code).toString() + "\n";
        if (isTW) {
            sendMsg = sendMsg + sendTw(isQian, isZhong, isHou);
        }else if (isJP) {
            sendMsg = sendMsg + sendJp(isQian, isZhong, isHou);
        }else if (isCN) {
            sendMsg = sendMsg + sendCn(isQian, isZhong, isHou);
        }
        sender.SENDER.sendGroupMsg(groupCode, sendMsg);
    }

    private String sendTw(boolean isQian, boolean isZhong, boolean isHou) {
        String sendMsg = "※表格仅供参考，升r有风险，强化需谨慎\n"
                + "※不定期搬运自图中群号\n"
                + "※图中广告为原作者推广，与本bot无关\n"
                + "Rank推荐表：\n";
        final String rank_tw_qian_path = "/tmp/pcr/images/rank_tw/qian.png";
        final String rank_tw_zhong_path = "/tmp/pcr/images/rank_tw/zhong.png";
        final String rank_tw_hou_path = "/tmp/pcr/images/rank_tw/hou.png";
        if (isQian || isZhong || isHou) {
            if (isQian) {
                CQCode cqCode_image_qian = CQCodeUtil.build().getCQCode_Image(rank_tw_qian_path);
                sendMsg = sendMsg + cqCode_image_qian.toString();
            }else if (isZhong) {
                CQCode cqCode_image_zhong = CQCodeUtil.build().getCQCode_Image(rank_tw_zhong_path);
                sendMsg = sendMsg + cqCode_image_zhong.toString();
            }else {
                CQCode cqCode_image_hou = CQCodeUtil.build().getCQCode_Image(rank_tw_hou_path);
                sendMsg = sendMsg + cqCode_image_hou.toString();
            }
        }else {
            CQCode cqCode_image_qian = CQCodeUtil.build().getCQCode_Image(rank_tw_qian_path);
            CQCode cqCode_image_zhong = CQCodeUtil.build().getCQCode_Image(rank_tw_zhong_path);
            CQCode cqCode_image_hou = CQCodeUtil.build().getCQCode_Image(rank_tw_hou_path);
            sendMsg = sendMsg + cqCode_image_qian.toString() + "\n"
                    + cqCode_image_zhong.toString() + "\n"
                    + cqCode_image_hou.toString();
        }
        return sendMsg;
    }
    private String sendJp(boolean isQian, boolean isZhong, boolean isHou) {
        String sendMsg = "※表格仅供参考，升r有风险，强化需谨慎\n"
                + "※不定期搬运自图中群号\n"
                + "※图中广告为原作者推广，与本bot无关\n"
                + "Rank推荐表：\n";
        String rank_jp_qian_path = "/tmp/pcr/images/rank_jp/qian.png";
        String rank_jp_zhong_path = "/tmp/pcr/images/rank_jp/zhong.png";
        String rank_jp_hou_path = "/tmp/pcr/images/rank_jp/hou.png";
        if (isQian || isZhong || isHou) {
            if (isQian) {
                CQCode cqCode_image_qian = CQCodeUtil.build().getCQCode_Image(rank_jp_qian_path);
                sendMsg = sendMsg + cqCode_image_qian.toString();
            }else if (isZhong) {
                CQCode cqCode_image_zhong = CQCodeUtil.build().getCQCode_Image(rank_jp_zhong_path);
                sendMsg = sendMsg + cqCode_image_zhong.toString();
            }else {
                CQCode cqCode_image_hou = CQCodeUtil.build().getCQCode_Image(rank_jp_hou_path);
                sendMsg = sendMsg + cqCode_image_hou.toString();
            }
        }else {
            CQCode cqCode_image_qian = CQCodeUtil.build().getCQCode_Image(rank_jp_qian_path);
            CQCode cqCode_image_zhong = CQCodeUtil.build().getCQCode_Image(rank_jp_zhong_path);
            CQCode cqCode_image_hou = CQCodeUtil.build().getCQCode_Image(rank_jp_hou_path);
            sendMsg = sendMsg + cqCode_image_qian.toString() + "\n"
                    + cqCode_image_zhong.toString() + "\n"
                    + cqCode_image_hou.toString();
        }
        return sendMsg;
    }

    private String sendCn(boolean isQian, boolean isZhong, boolean isHou) {
        String sendMsg = "※表格仅供参考，升r有风险，强化需谨慎\n"
                + "※不定期搬运自图中群号\n"
                + "※图中广告为原作者推广，与本bot无关\n"
                + "Rank推荐表：\n";
        String rank_cn_tatal_path = "/tmp/pcr/images/rank_cn/total.png";
        CQCode cqCode_image_total = CQCodeUtil.build().getCQCode_Image(rank_cn_tatal_path);
        sendMsg = sendMsg + cqCode_image_total.toString();
        return sendMsg;
    }

}
