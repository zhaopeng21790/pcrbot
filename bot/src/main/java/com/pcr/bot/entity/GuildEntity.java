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
 * @date 2020-08-02 16:37:57
 */
@Data
@TableName("pcr_guild")
public class GuildEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 公会的名称
	 */
	private String name;

	/**
	 * 创建公会的QQ号
	 */
	private String QqCode;

	/**
	 * 群组ID
	 */
	private String groupCode;


	/**
	 * 1:台服；2：日服；3：国服
	 */
	private Integer rate;

	/**
	 * 公会的创建时间
	 */
	private Date expiredTime;

	/**
	 * 公会的创建时间
	 */
	private Date createTime;

}
