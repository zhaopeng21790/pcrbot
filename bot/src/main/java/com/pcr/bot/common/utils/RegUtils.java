package com.pcr.bot.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * @author Codi
 * @date 2020/8/5
 **/
public class RegUtils {


    /**
     * 是否为数字字符串
     */
    public static final String NUMBER_TEXT = "^([0-9]+)$";


    /**
     * 正则表达式校验,符合返回True
     * @param regex 正则表达式
     * @param content 校验的内容
     * @return
     */
    public static boolean isMatch(String regex, CharSequence content){
        return Pattern.matches(regex, content);
    }

    /**
     * 判断是否为数字字符串，如0011，10101，01
     * @param str
     * @return
     */
    public static boolean isNumberText(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return isMatch(NUMBER_TEXT, str);
    }
}
