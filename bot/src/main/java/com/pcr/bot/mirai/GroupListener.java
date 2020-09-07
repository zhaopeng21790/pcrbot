package com.pcr.bot.mirai;

import com.forte.qqrobot.anno.Async;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.template.OnGroup;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberIncrease;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberReduce;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.KeywordMatchType;
import com.forte.qqrobot.bot.BotManager;
import com.forte.qqrobot.sender.MsgSender;
import com.pcr.bot.command.AdminCommand;
import com.pcr.bot.command.GroupMemberChangeCommand;
import com.pcr.bot.common.exception.RRException;
import com.pcr.bot.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Codi
 * @date 2020/8/2
 **/

@Component
public class GroupListener {

    @Autowired
    private BotManager botManager;

    @Autowired
    private GroupService groupService;

    @Autowired
    GroupMemberChangeCommand groupMemberChangeCommand;

    @Autowired
    AdminCommand adminCommand;



    /**
     * <p> 测试此监听消息的方法：在机器人所在群发送任意消息。
     *
     * <p> 监听群消息，打印在控制台，不做任何回复。
     *
     * <p> {@link OnGroup}注解代表监听群消息，
     * 他等同于参数为{@link MsgGetTypes#groupMsg}的{@link Listen}
     *
     * <p> 监听注解相关的详细内容参考文档或入群询问：
     * <p> http://simple-robot-doc.forte.love/1408365
     * <p> http://simple-robot-doc.forte.love/1780853
     *
     * <p> 也就是说，@OnGroup 等同于 @Listen(MsgGetTypes.groupMsg)
     * <p> @OnGroup注解属于一种模板注解，其他类似的模板注解参考包路径{@link com.forte.qqrobot.anno.template}下的全部注解。
     *
     * @param groupMsg 由于你监听的是“群消息”，因此你的参数中可以填入私信消息对应的封装接口，即{@link GroupMsg} <br>
     *                 至于其他监听类型应该填写什么参数，你可以参考{@link MsgGetTypes}枚举的元素和他们的参数。基本上都是见明知意的东西。
     */
    @Async
    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg) throws RRException {
        MsgSender sender = botManager.defaultBot().getSender();
        try {
            groupService.handleCommand(groupMsg, sender);
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), e.getMessage());
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "面板", keywordMatchType = KeywordMatchType.RE_CQCODE_TRIM_EQUALS)
    public void guildWeb(GroupMsg groupMsg, MsgSender sender) {
        try {
            String sendMsg = "公会战面板：\n" +
                    "http://101.200.33.173:9080/#/dashboard\n" +
                    "建议添加到浏览器收藏夹或桌面快捷方式";
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sendMsg);
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), e.getMessage());
        }
    }

    /**
     * 群成员增加
     */
    @Async
    @Listen(MsgGetTypes.groupMemberIncrease)
    public void onGroupListener1(GroupMemberIncrease groupMemberIncrease) throws RRException {
        groupMemberChangeCommand.groupMemberIncrease(groupMemberIncrease);
    }

    /**
     * 群成员减少
     */
    @Async
    @Listen(MsgGetTypes.groupMemberReduce)
    public void onGroupListener2(GroupMemberReduce groupMemberIncrease) throws RRException {
        groupMemberChangeCommand.groupMemberReduce(groupMemberIncrease);
    }







    /**
     * <p> 测试此监听消息的方法：在机器人所在at他并说：hi
     *
     * <p> 第二个示例，依旧是监听群聊消息，但是多了一个注解：{@link Filter}
     *
     * <p> filter用来过滤接收到的消息的内容，其参数很多，且支持自定义过滤器，详细的相关内容请查阅文档或入群询问 :
     * <p> http://simple-robot-doc.forte.love/1408366
     * <p> http://simple-robot-doc.forte.love/1780854
     * <p> http://simple-robot-doc.forte.love/1536507
     *
     * <p> 此处介绍的主要是filter的at功能与keywordMatchType参数与其中的坑。
     * <p> {@link Filter#at()}参数代表是否只有被at的时候才会触发此监听。听上去很简单，不是吗？
     * 但是首先你要知道，at在qq中也属于消息中的一部分，以Mirai为例，Mirai会将at消息转化为对应的Mirai码，
     * 因此当有人at机器人，并说了一句“hi”的时候，它实际接收到的消息是：“[mirai:at:123456789,@qq昵称] hi”而不是 “hi”或者 “@qq昵称 hi”。
     * 这个时候，假如你不修改{@link Filter#keywordMatchType()}参数的话，你直接匹配“hi”是无法匹配到内容的。
     *
     * <p> keywordMatchType参数内容是枚举{@link KeywordMatchType}，它代表了filter使用什么方式对消息进行匹配，例如正则、equals、contains等。
     * 此示例中，我的参数为{@link KeywordMatchType#RE_CQCODE_TRIM_EQUALS}，它代表：移除消息中的CQ码，并将消息执行trim(), 然后使用equals进行匹配。
     * 因此，at消息中的CQ码移除再trim后，留下的就是你想要匹配的“hi”了。
     *
     * <p> 至于枚举{@link KeywordMatchType}中都有哪些匹配方式，你可以参考文档或者直接点进去看看。基本上枚举名的含义很好猜的，我也全部写了注释。
     *
     * <p> 简单来说@{@link Filter#value()}代表的消息匹配的内容，例如你要监听hi的话就如同下面这个demo所示
     * 如果{@link Filter#keywordMatchType()}不能蛮族你的需求，同时需求并不复杂，可以考虑使用正则表达式来进行监听
     * 例如 @Filter(value = ".*hi.*")就是用来监听所说的话中包含hi的对话
     *
     * @param groupMsg 由于你监听的是“群消息”，因此你的参数中可以填入私信消息对应的封装接口，即{@link GroupMsg} <br>
     *                 至于其他监听类型应该填写什么参数，你可以参考{@link MsgGetTypes}枚举的元素和他们的参数。基本上都是见明知意的东西。
     * @param sender   送信器。所有的监听函数都可以注入这个类，它包含了三大送信器来支持你发送、获取消息。当然，此方法中没有
     *                 sender.SENDER.sendGroupMsg(groupMsg, sendMsg) 之中 groupMsg其实是指群号，sendMsg是需要发送的消息，所以也可以写成
     *                 sender.SENDER.sendGroupMsg(“qq群号”, “消息内容”)
     */
    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "hi", at = true, keywordMatchType = KeywordMatchType.RE_CQCODE_TRIM_EQUALS)
    public void whoAreYou(GroupMsg groupMsg, MsgSender sender) {
        // 红色显眼儿一点
        String sendMsg = "hello,你At人家干嘛";
        // 发送群消息。下述几个方法效果相同。
        sender.SENDER.sendGroupMsg(groupMsg, sendMsg);

    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "新增管理员", at = true, keywordMatchType = KeywordMatchType.RE_CQCODE_TRIM_EQUALS)
    public void addAdminQQ(GroupMsg groupMsg, MsgSender sender) {
        try {
            adminCommand.handlerAdd(groupMsg, sender);
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), e.getMessage());
        }
    }


    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "移除管理员", at = true, keywordMatchType = KeywordMatchType.RE_CQCODE_TRIM_EQUALS)
    public void reduceAdminQQ(GroupMsg groupMsg, MsgSender sender) {
        try {
            adminCommand.handlerReduce(groupMsg, sender);
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), e.getMessage());
        }
    }

    @Listen(MsgGetTypes.groupMsg)
    @Filter(value = "flush", at = true, keywordMatchType = KeywordMatchType.RE_CQCODE_TRIM_EQUALS)
    public void flushAdminQQ(GroupMsg groupMsg, MsgSender sender) {
        try {
            adminCommand.flushAdmin(groupMsg, sender);
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroup(), e.getMessage());
        }
    }





}
