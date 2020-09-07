package com.pcr.bot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-03 18:15:56
 */
@Data
@TableName("pcr_boss_status")
public class BossStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * QQ群号
	 */
	@TableId
	private String groupCode;
	/**
	 * boss的血量
	 */
	private Integer bossBlood;
	/**
	 * boss的总血量
	 */
	private Integer totalBlood;
	/**
	 * boss的序号
	 */
	private Integer bossNum;
	/**
	 * 第几周目
	 */
	private Integer weekNum;
	/**
	 * 会战的日期：2020-07
	 */
	@TableId
	private String guildDate;
	/**
	 *
	 */
	private Date updateTime;
	/**
	 *
	 */
	private Date createTime;

}
