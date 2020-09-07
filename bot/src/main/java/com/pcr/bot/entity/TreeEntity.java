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
 * @date 2020-08-04 17:36:30
 */
@Data
@TableName("pcr_tree")
public class TreeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * qq群ID
	 */
	private String groupCode;
	/**
	 * qq号
	 */
	private String qqCode;
	/**
	 * qq昵称
	 */
	private String nickname;
	/**
	 * 留言
	 */
	private String message;
	/**
	 * boss序列号
	 */
	private Integer bossNum;
	/**
	 * 日期：2020-08-09
	 */
	private String day;
	/**
	 * 0:挂树；1:SL
	 */
	private Integer type;
	/**
	 *
	 */
	private Date createTime;

}
