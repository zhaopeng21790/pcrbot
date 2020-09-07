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
 * @date 2020-08-04 11:44:14
 */
@Data
@TableName("pcr_knife_history")
public class KnifeHistoryEntity implements Serializable {
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
	 * 出刀的QQ号的昵称
	 */
	private String nickname;

	/**
	 * 第几周目
	 */
	private Integer weekNum;

	/**
	 * 第几周目
	 */
	private Integer isKilled;

	/**
	 * 哪一天
	 */
	private String day;
	/**
	 * 0:完整刀；1:补偿刀
	 */
	private Integer status;
	/**
	 * 今天的第几刀
	 */
	private Integer seqId;
	/**
	 * 0:没有补偿时间；1:有补偿时间；
	 */
	private Integer isRewardKnife;
	/**
	 * 对boss造成的伤害值
	 */
	private Integer damage;
	/**
	 * 对几号boss造成的伤害
	 */
	private Integer bossNum;
	/**
	 *
	 */
	private Date createTime;

}
