package com.pcr.bot.common.utils;

import lombok.Getter;

/**
 * @author Codi
 * @date 2020/8/2
 **/
public class Constant {

    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;

    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";


    @Getter
    public enum Command {
        help(0, "帮助"),
        //创建公会
        create_guild(-1, "^创建([日台国陆Bb])服?([公工]){1}会$"),
        //加入公会 [@某人]	加入到公会名单，如果有 at 则为加入他人
        join_guild(1, "^加入[工公]?会$"),
        //加入全部成员
        join_all_to_guild(2, "加入全部成员"),
        exit_guild(2, "退会"),
        //报刀2000000，对boss造成伤害但未击败时用，记录伤害，可以使用 200w/200万/2000k 等，如果有at则为代报
        //尾刀 [@某人] [昨日] [:留言],对 boss 造成伤害并击败时用，记录伤害，如果有 at 则为代报，如果有“昨日”则将记录添加到前一天
        kill_boss(4, "尾刀"),
        //SL或sl,挑战 boss 强制取消后用，记录本日 SL，用?sl查询今日是否已 SL
        //申请出刀
        apply_knife(5,"申请出刀"),
        cancel_apply_knife(5,"取消出刀"),
        //撤销,	撤销上一次报刀（非管理员只能撤销自己的记录）
        undo_knife(6, "撤销报刀"),
        //状态, 显示 boss 状态
        status(7, "状态"),
        //预约1 [:留言]	预约 boss，当 boss 出现时通知
        subscribe_boss(8, "^预约[1-5]$"),
        //取消预约1 / 取消1
        cancel_subscribe_boss(10, "^取消[预约]*[1-5]$"),
        //锁定:留言	锁定 boss，提醒后面的人暂停出刀，冒号后为留言
        lock_boss(11, "锁定"),
        //解锁 boss，其他人可以继续申请
        unlock_boss(12, "解锁"),
        //查询预约boss的状态
        check_subscribe(14, "^查[1-5]$"),
        check_tree(15, "查树"),
        sl(16, "SL"),
        check_sl(16, "查SL"),
        cancel_sl(16, "取消SL"),
        //挂树 [:留言]	挂树，当 boss 被击败时通知
        link_tree(17, "挂树"),
        un_link_tree(17, "下树"),
        check_all_subscript(18, "查全部"),
        check_current_day_knife(19, "查刀"),
        check_yesterday_day_knife(19, "昨日报告"),
        check_any_day_knife(19, "^202[0-9]-[0-1][0-9]-[0-3][0-9]报告$"),
        edit_week_num(20, "^修正周目=[1-9][0-9]*$"),
        edit_boss_num(20, "^修正boss=[1-5]{1}$"),
        edit_boss_blood(20, "^修正血量=[1-9][0-9]*$"),
        boss_init(20, "重置boss"),
        report_damage(20, "^报刀[\\s]?[1-9][0-9]*[万]?[w]?[W]?"),
        rank_table(100, "^(\\*?([日台国陆Bb])服?([前中后]*)卫?)?rank(表|推荐|指南)?$"),
        knife_tips(20, "催刀"),
        knife_tips_1(20, "提醒出刀"),
        wakuang(30, "^挖矿[1-9][0-9]*$"),
        tw_news(40, "台服新闻"),
        cn_news(40, "国服新闻")
        ;

        private int value;
        private String command;

        Command(int value, String command) {
            this.value = value;
            this.command = command;
        }
    }

    @Getter
    public enum Error {
        qq_split_exception(-10000, " 格式解析错误"),
        no_permissions(10000,"该命令只能管理员操作"),
        no_guild_name(10001, "缺少公会名称"),
        exist_guild(10002, "该群组已经创建过公会"),
        no_at_person(10003, "需要@一个用户"),
        boss_num_exception(10004, "boss序列号在[1, 5]"),
        is_subscribed(10005, "您已经预约过了"),
        not_subscribed(10006, "您还没有预约"),
        is_locked(10007, "当前已锁定，请先解锁"),
        not_guild(10008, "还未创建公会，请联系管理员先创建公会"),
        knife_is_exists(10008, "申请失败..."),
        boss_blood_exception(10009, "boss的血量不能减少为负值"),
        knife_out(10010, "今日出刀次数已经用完"),
        knife_report_fail_not_apply(10011, "报刀失败，您还未申请出刀"),
        not_guild_in_group(10012,"请先创建公会"),
        not_in_guild_of_qq(10012,"请先加入公会后在使用会战命令"),
        ;
        private int value;
        private String msg;

        Error(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }
    }

    @Getter
    public enum Lock {
        unlock(0),
        lock(1),
        ;

        private int value;

        Lock(int value) {
            this.value = value;
        }
    }

    @Getter
    public enum Knife {
        full(0),
        reward(1),
        ;
        private int value;

        Knife(int value) {
            this.value = value;
        }
    }

    @Getter
    public enum Type {
        SL(1),
        TREE(0),
        ;
        private int value;

        Type(int value) {
            this.value = value;
        }
    }

    @Getter
    public enum Rate {
        tw(1),
        jp(2),
        cn(3)
        ;
        private int value;

        Rate(int value) {
            this.value = value;
        }
    }



    @Getter
    public enum URL {
        pcr_python_host("http://58.87.103.162:8888"),
        ;
        private String value;

        URL(String value) {
            this.value = value;
        }


    }

}
