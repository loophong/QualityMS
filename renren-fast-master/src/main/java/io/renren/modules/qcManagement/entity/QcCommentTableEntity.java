package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-10-28 16:16:44
 */
@Data
@TableName("qc_comment_table")
public class QcCommentTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Integer qcTableId;
	/**
	 * 初评或终评
	 */
	private String tableType;
	/**
	 * 
	 */
	private String tableNumber;
	/**
	 * 小标题
	 */
	private String tableTag;
	/**
	 * 评审内容
	 */
	private String tableComment;
	/**
	 * 备注
	 */
	private String tableExtra;

}
