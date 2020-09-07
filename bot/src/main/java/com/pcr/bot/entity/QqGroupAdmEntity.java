package com.pcr.bot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * QQ群组的管理员
 *
 * @author Codi
 * @email codi.peng.zhao@gmail.com
 * @date 2020-08-02 17:03:05
 */
@Data
@TableName("pcr_qq_group_adm")
public class QqGroupAdmEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 群组ID
	 */
	private String groupCode;
	/**
	 * 当前群组的管理员QQ号
	 */
	private String QqCode;
	/**
	 *
	 */
	private Date createTime;
	/**
	 *
	 */
	private Date updateTime;

}
