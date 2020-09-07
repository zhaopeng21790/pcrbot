package com.pcr.bot.command;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.messages.result.GroupInfo;
import com.forte.qqrobot.beans.messages.result.GroupMemberList;
import com.forte.qqrobot.beans.messages.result.inner.GroupMember;
import com.forte.qqrobot.bot.BotManager;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.sender.senderlist.SenderGetList;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.common.utils.Constant;
import com.pcr.bot.common.utils.RestTemplateUtil;
import com.pcr.bot.entity.NewsEntity;
import com.pcr.bot.entity.QqGroupAdmEntity;
import com.pcr.bot.service.GuildPersonService;
import com.pcr.bot.service.QqGroupAdmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Codi
 * @date 2020/8/5
 **/
@Component
public class CommandUtils {

    @Autowired
    BotManager botManager;
    @Autowired
    QqGroupAdmService qqGroupAdmService;
    @Autowired
    GuildPersonService guildPersonService;



    public boolean isAdmin(String groupCode, String qqCode) {
        MsgSender sender = botManager.defaultBot().getSender();
        GroupInfo groupInfo = sender.getGroupInfoByCode(groupCode);
        Map<String, String> adminNickList = groupInfo.getAdminNickList();
        if (adminNickList != null && adminNickList.containsKey(qqCode)) return true;
        if (groupInfo.getOwnerQQ().equals(qqCode)) return true;
        QqGroupAdmEntity qqGroupAdmEntity = qqGroupAdmService.getOne(groupCode, qqCode);
        if (qqGroupAdmEntity == null) return false;
        return true;
    }


    /**
     * 检查指定QQ是否在群组的公会中
     */
    public void checkCodeInGuild(String groupCode, String code) {
        boolean existInGroupOfQQ = guildPersonService.isExist(groupCode, code);
        if (!existInGroupOfQQ) throw new RRException(Constant.Error.not_in_guild_of_qq);
    }


    /**
     * 检查指定QQ是否在群组的公会中
     */
    public void checkCodeInGuild(String groupCode, String code, String nickname) {
        boolean existInGroupOfQQ = guildPersonService.isExist(groupCode, code);
        if (!existInGroupOfQQ) throw new RRException(nickname + "未加入公会，" + Constant.Error.not_in_guild_of_qq.getMsg());
    }


    /**
     * 取群成员列表
     * @param groupCode 群号
     * @return  成员列表
     */
    public List<String> getAllQQFromGroupCode(String groupCode) {
        MsgSender sender = botManager.defaultBot().getSender();
        //获取当前在群组中的人员，防止有人退群导致@消息的失败
        SenderGetList senderGetList = sender.GETTER;
        GroupMemberList groupMemberList = senderGetList.getGroupMemberList(groupCode);
        GroupMember[] groupMembers = groupMemberList.getList();
        return Arrays.stream(groupMembers).map(GroupMember::getQQ).collect(Collectors.toList());
    }

    //获取国服新闻
    public ArrayList<NewsEntity> getCNNew() {
        ArrayList<NewsEntity> list = new ArrayList<>();
        String url = Constant.URL.pcr_python_host.getValue() + "/news?ev=3";
        JSONObject cnNewsJsonObject = RestTemplateUtil.getForm(url, JSONObject.class);
        String cnErrMsg =  cnNewsJsonObject.getString("message");
        if (StringUtils.isNotBlank(cnErrMsg)) {
            return list;
        }
        JSONArray jsonArray = cnNewsJsonObject.getJSONArray("data");
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            NewsEntity newsEntity = getNewsEntity(jsonObject);
            list.add(newsEntity);
        }
        return list;
    }

    //获取台服新闻
    public ArrayList<NewsEntity> getTWNew() {
        ArrayList<NewsEntity> list = new ArrayList<>();
        String url = Constant.URL.pcr_python_host.getValue() + "/news?ev=1";
        JSONObject twNewsJsonObject = RestTemplateUtil.getForm(url, JSONObject.class);
        String cnErrMsg =  twNewsJsonObject.getString("message");
        if (StringUtils.isNotEmpty(cnErrMsg)) {
            return list;
        }
        JSONArray jsonArray = twNewsJsonObject.getJSONArray("data");
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            NewsEntity newsEntity = getNewsEntity(jsonObject);
            list.add(newsEntity);
        }
        return list;
    }


    private NewsEntity getNewsEntity(JSONObject jsonObject) {
        String base_url = jsonObject.getString("base_url");
        String title = jsonObject.getString("title");
        String url = jsonObject.getString("url");
        String ctime = jsonObject.getString("ctime");
        Integer environment = jsonObject.getInteger("environment");
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setBaseUrl(base_url);
        newsEntity.setTitle(title);
        newsEntity.setUrl(url);
        newsEntity.setCtime(ctime);
        newsEntity.setEnvironment(environment);
        return newsEntity;
    }




}
