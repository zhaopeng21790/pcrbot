package com.pcr.bot.models;

import com.forte.qqrobot.beans.cqcode.CQCode;
import lombok.Data;

/**
 * @author Codi
 * @date 2020/8/2
 **/
@Data
public class QQModel {

    private String qqCode;
    private String groupCode;
    private String nickname;

    public QQModel() {
    }

    public QQModel(CQCode cqCode) {
        String qqCode = cqCode.getParam("qq");
        this.nickname = cqCode.getParam("display").replace("@", "");
        this.qqCode = qqCode;
    }
}
