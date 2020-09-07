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
 * @date 2020-08-03 10:50:52
 */
@Data
@TableName("pcr_group_status")
public class GroupStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * QQ群号
	 */
	@TableId
	private String groupCode;
	/**
	 * 0:正常;1:锁定;
	 */
	private Integer status;
	/**
	 * 
	 */
	private Date createTime;

}
