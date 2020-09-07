package com.pcr.bot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2020-08-02 23:05:15
 */
@Data
@TableName("pcr_subscribe_boss")
public class SubscribeBossEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * boss序号
	 */
	private Integer bossNum;
	/**
	 * 预约人员的QQ号
	 */
	private String qqCode;
	/**
	 * 预约人员的QQ昵称
	 */
	private String nickname;
	/**
	 * 留言
	 */
	private String message;
	/**
	 * 预约boss的时间：2020-08-02
	 */
	private String subscribeTime;
	/**
	 * 预约boss的群组
	 */
	private String groupCode;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
