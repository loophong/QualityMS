package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 16:54:01
 */
@Data
@TableName("issue_mask_table")
public class IssueMaskTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer issuemaskId;
	/**
	 * 编号
	 */
	private String serialNumber;
	/**
	 * 问题编号(所属问题)
	 */
	private String issueNumber;
	/**
	 * 审核人
	 */
	private String reviewers;
	/**
	 * 审核意见
	 */
	private String reviewerOpinion;
	/**
	 * 接收人
	 */
	private String recipients;
	/**
	 * 任务内容
	 */
	private String maskcontent;
	/**
	 * 处理方案
	 */
	private String handlingScenarios;
	/**
	 * 附件
	 */
	private String annex;
	/**
	 * 任务发起人
	 */
	private String creator;
	/**
	 * 整改责任人
	 */
	private String rectificationResponsiblePerson;
	/**
	 * 任务创建人
	 */
	private String issuecreator;
	/**
	 * 发起时间
	 */
	private Date creationTime;
	/**
	 * 要求完成时间
	 */
	private Date requiredCompletionTime;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 上级任务
	 */
	private String superiorMask;
	/**
	 * 下级任务
	 */
	private String nextMask;

}
