package com.pcr.bot.common.utils;

/**
 * @author Codi
 * @date 2020/8/5
 **/
public class StringUtils {

    /**
     * 提取字符串中的数字
     * 第一次遇到数字且后续遇到非数字字符停止
     */
    public static String getNumberText(String str){
        if(org.apache.commons.lang.StringUtils.isBlank(str)){
            throw new RuntimeException("参数str不能为空");
        }
        StringBuffer number = new StringBuffer("");
        String[] strArray = str.split("");
        boolean flag = false;
        for (int i=1; i<strArray.length; i++) {
            if(RegUtils.isNumberText(strArray[i])){
                number.append(strArray[i]);
                flag = true;
            }else if(flag){
                break;
            }
        }
        return number.toString();
    }
}
