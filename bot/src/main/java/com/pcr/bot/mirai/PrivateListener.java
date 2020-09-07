package com.pcr.bot.mirai;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.template.OnPrivate;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.KeywordMatchType;
import com.pcr.bot.command.CommandUtils;
import com.pcr.bot.command.UserCommand;
import com.pcr.bot.entity.NewsEntity;
import com.pcr.bot.service.NewsService;
import com.simbot.component.mirai.messages.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Codi
 * @date 2020/8/2
 **/

@Component
public class PrivateListener {

    @Autowired
    UserCommand userCommand;
    @Autowired
    CommandUtils commandUtils;
    @Autowired
    NewsService newsService;

    /**
     * <p> 测试此监听消息的方法：私聊机器人发送任意消息。
     *
     * <p> 监听私信消息，打印在控制台，不做任何回复。
     *
     * <p> {@link OnPrivate}注解代表监听私信消息，
     * 他等同于参数为{@link MsgGetTypes#privateMsg}
     *
     * <p> 监听注解相关的详细内容参考文档或入群询问：
     * <p> http://simple-robot-doc.forte.love/1408365
     * <p> http://simple-robot-doc.forte.love/1780853
     *
     * <p> 也就是说，@OnPrivate 等同于 @Listen(MsgGetTypes.privateMsg)
     * <p> @OnPrivate注解属于一种模板注解，其他类似的模板注解参考包路径{@link com.forte.qqrobot.anno.template}下的全部注解。
     *
     * @param priMsg 由于你监听的是“私信消息”，因此你的参数中可以填入私信消息对应的封装接口，即{@link PrivateMsg} <br>
     *               至于其他监听类型应该填写什么参数，你可以参考{@link MsgGetTypes}枚举的元素和他们的参数。基本上都是见明知意的东西。
     */
    @OnPrivate
    @Filter(value = "注册-", keywordMatchType = KeywordMatchType.TRIM_STARTS_WITH)
    public Reply privateRegisterMsg(PrivateMsg priMsg) {
        try {
            return userCommand.registerUser(priMsg, null);
        } catch (Exception e) {
            return Reply.getMessageReply(e.getMessage());
        }
    }

    @OnPrivate
    @Filter(value = "重置密码-", keywordMatchType = KeywordMatchType.TRIM_STARTS_WITH)
    public Reply resetPasswordMsg(PrivateMsg priMsg) {
        try {
            return userCommand.resetPassword(priMsg, null);
        } catch (Exception e) {
            return Reply.getMessageReply(e.getMessage());
        }
    }


    @OnPrivate
    @Filter(value = "news-job-execute", keywordMatchType = KeywordMatchType.EQUALS)
    public Reply newsJobExecute(PrivateMsg priMsg) {
        try {
            ArrayList<NewsEntity> cnNewList = commandUtils.getCNNew();
            ArrayList<NewsEntity> arrayList = new ArrayList<>();
            for (NewsEntity entity : cnNewList) {
                if (!newsService.isExistsNews(entity.getUrl())) {
                    arrayList.add(entity);
                }
            }
            newsService.saveOrUpdateBatch(arrayList);
            return Reply.getMessageReply("ok");
        } catch (Exception e) {
            return Reply.getMessageReply(e.getMessage());
        }
    }

}
