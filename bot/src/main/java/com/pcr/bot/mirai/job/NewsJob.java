package com.pcr.bot.mirai.job;

import com.forte.qqrobot.anno.timetask.CronTask;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.timetask.TimeJob;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.pcr.bot.command.CommandUtils;
import com.pcr.bot.common.utils.SpringUtil;
import com.pcr.bot.entity.GuildEntity;
import com.pcr.bot.entity.NewsEntity;
import com.pcr.bot.service.GuildService;
import com.pcr.bot.service.NewsService;
import com.pcr.bot.service.impl.GuildServiceImpl;
import com.pcr.bot.service.impl.NewsServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Codi
 * @date 2020/9/5
 **/
@Slf4j
@CronTask("0 0/5 12 * * ?")
public class NewsJob implements TimeJob {

    NewsService newsService =  SpringUtil.getBean(NewsServiceImpl.class);

    GuildService guildService =  SpringUtil.getBean(GuildServiceImpl.class);

    CommandUtils commandUtils =  SpringUtil.getBean(CommandUtils.class);


    /// 每天中午12点，每隔5分钟，获取一次最新的新闻
    @Override
    public void execute(MsgSender msgSender, CQCodeUtil cqCodeUtil) {
        log.info("job execute");
        //获取国服新闻
        getCNNew(msgSender, cqCodeUtil);
        //获取台服新闻
        getTWNew(msgSender, cqCodeUtil);
    }

    //获取国服新闻
    private void getCNNew(MsgSender msgSender, CQCodeUtil cqCodeUtil) {
        ArrayList<NewsEntity> cnNewList = commandUtils.getCNNew();
        ArrayList<NewsEntity> newsEntities = saveNews(cnNewList);
        if (newsEntities.size() == 0) return;
        StringBuilder sb = new StringBuilder("国服官网新闻\n");
        for (NewsEntity newsEntity : newsEntities) {
            sb.append(newsEntity.getTitle())
                    .append("\n")
                    .append("▲")
                    .append(newsEntity.getBaseUrl())
                    .append(newsEntity.getUrl())
                    .append("\n");
        }
        List<GuildEntity> allTWGuild = guildService.findAll(3);
        for (GuildEntity guildEntity : allTWGuild) {
            try {
                msgSender.SENDER.sendGroupMsg(guildEntity.getGroupCode(), sb.toString().trim());
            } catch (Exception e) {
                log.error("send new error: " + guildEntity.getGroupCode());
            }
        }
    }

    //获取台服新闻
    private void getTWNew(MsgSender msgSender, CQCodeUtil cqCodeUtil) {
        ArrayList<NewsEntity> newsList = commandUtils.getTWNew();
        ArrayList<NewsEntity> newsEntities = saveNews(newsList);
        if (newsEntities.size() == 0) return;
        StringBuilder sb = new StringBuilder("台服官网新闻\n");
        for (NewsEntity newsEntity : newsEntities) {
            sb.append(newsEntity.getTitle())
                    .append("\n")
                    .append("▲")
                    .append(newsEntity.getBaseUrl())
                    .append(newsEntity.getUrl())
                    .append("\n");
        }
        List<GuildEntity> allTWGuild = guildService.findAll(1);
        if (allTWGuild == null) return;
        for (GuildEntity guildEntity : allTWGuild) {
            try {
                msgSender.SENDER.sendGroupMsg(guildEntity.getGroupCode(), sb.toString().trim());
            } catch (Exception e) {
                log.error("send new error: " + guildEntity.getGroupCode());
            }
        }
    }


    private ArrayList<NewsEntity> saveNews(ArrayList<NewsEntity> list) {
        ArrayList<NewsEntity> arrayList = new ArrayList<>();
        for (NewsEntity entity : list) {
            if (!newsService.isExistsNews(entity.getUrl())) {
                arrayList.add(entity);
            }
        }
        newsService.saveOrUpdateBatch(arrayList);
        return arrayList;
    }
}
