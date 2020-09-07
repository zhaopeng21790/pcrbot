package com.pcr.bot.common.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author Codi
 * @date 2020/9/5
 **/
public class RestTemplateUtil {


    /**
     * 发送表单参数的post请求
     *
     * @param url      请求url
     * @param param    参数
     * @param respType 返回类型
     * @return T
     */
    public static <T> T postForm(String url, Map<String, List<Object>> param, Class<T> respType) {

        return getRestInstance().postForEntity(url, getHttpEntity(param, false), respType).getBody();
    }


    /**
     * 发送表单有参数get请求
     *
     * @param url      请求url
     * @param param    参数对象
     * @param respType 返回类型
     * @return T
     */
    public static <T> T getForm(String url, Class<T> respType, Map<String,String> param) {
        return getRestInstance().getForEntity(url, respType, param).getBody();
    }

    /**
     * @Description: 发送表单无参数的get请求
     * @Param: [url, param, respType]
     * @return: T
     * @Author: tonyzhang
     * @Date: 2019-01-18 17:23
     */
    public static <T> T getForm(String url, Class<T> respType) {
        return getRestInstance().getForObject(url, respType);
    }


    /**
     * 获取HttpEntity实例对象
     *
     * @param param  参数对象
     * @param isJson true 发送json请求,false发送表单请求
     * @return HttpEntity
     */
    private static <P> HttpEntity<P> getHttpEntity(P param, boolean isJson) {
        HttpHeaders headers = new HttpHeaders();
        if (isJson) {
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        } else {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        return new HttpEntity<>(param, headers);
    }

    private static RestTemplate getRestInstance() {
        return SpringUtil.getBean(RestTemplate.class);
    }
}
