package com.pcr.bot.command;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.entity.NewsEntity;
import com.pcr.bot.service.NewsService;
import com.simplerobot.modules.utils.KQCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Codi
 * @date 2020/9/5
 **/
@Slf4j
@Component
public class NewsCommand {
    @Autowired
    CommandUtils commandUtils;
    @Autowired
    NewsService newsService;


    public void handlerTwNews(GroupMsg groupMsg, MsgSender sender) {
        List<NewsEntity> news = newsService.getNews(4, 1);
        String baseUrl = "";
        StringBuilder sb = new StringBuilder("台服官网新闻\n");
        for (NewsEntity newsEntity : news) {
            baseUrl = newsEntity.getBaseUrl();
            sb.append(newsEntity.getTitle())
                    .append("\n")
                    .append("▲")
                    .append(newsEntity.getBaseUrl())
                    .append(newsEntity.getUrl())
                    .append("\n");
        }
        sb.append("更多消息查询:").append(baseUrl);
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), sb.toString().trim());
    }

    public void handlerCnNews(GroupMsg groupMsg, MsgSender sender) {
        List<NewsEntity> news = newsService.getNews(4, 3);
        String baseUrl = "";
        StringBuilder sb = new StringBuilder("国服官网新闻\n");
        for (NewsEntity newsEntity : news) {
            baseUrl = newsEntity.getBaseUrl();
            sb.append(newsEntity.getTitle())
                    .append("\n")
                    .append(newsEntity.getBaseUrl())
                    .append(newsEntity.getUrl())
                    .append("\n");
        }
        sb.append("更多消息查询:").append(baseUrl);
        CQCode cqCode_at = CQCodeUtil.build().getCQCode_At(groupMsg.getQQ());
        sender.SENDER.sendGroupMsg(groupMsg.getGroup(), cqCode_at + sb.toString().trim());
    }
}
