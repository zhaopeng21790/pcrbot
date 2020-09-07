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
 * @date 2020-08-03 19:51:50
 */
@Data
@TableName("pcr_boss_section_info")
public class BossSectionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 1:台服；2：日服；3：国服
	 */
	private String rate;

	/**
	 * 得分系数
	 */
	private float score;

	/**
	 *
	 */
	private Integer section;
	/**
	 *
	 */
	private Integer bossNum;
	/**
	 *
	 */
	private Integer bossBlood;
	/**
	 *
	 */
	private Date createTime;

}
