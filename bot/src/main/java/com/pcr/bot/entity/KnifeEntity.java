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
 * @date 2020-08-03 18:15:56
 */
@Data
@TableName("pcr_knife")
public class KnifeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * QQ群号
	 */
	private String groupCode;
	/**
	 * 申请出刀的QQ号
	 */
	private String qqCode;

	/**
	 * 申请出刀的QQ号
	 */
	private String nickname;
	/**
	 * 申请出刀对于的boss号
	 */
	private Integer bossNum;
	/**
	 *
	 */
	private Date createTime;

}
