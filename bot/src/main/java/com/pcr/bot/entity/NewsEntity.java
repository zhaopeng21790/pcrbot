package com.pcr.bot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 16:37:57
 */
@Data
@TableName("pcr_news")
public class NewsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;
	private Integer environment;
	private String baseUrl;
	private String url;
	private String title;
	private String ctime;
	@JsonIgnore
	private Date createTime;

}
