package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务审批表
 * 
 * @author sun
 * @email ${email}
 * @date 2024-12-15 14:35:09
 */
@Data
@TableName("task_management_plan_approval_table")
public class PlanApprovalTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 审批编号
	 */
	@TableId
	private Long approvalId;
	/**
	 * 任务ID
	 */
	private String planId;
	/**
	 * 任务名
	 */
	private String planName;
	/**
	 * 任务内容
	 */
	private String planContent;
	/**
	 * 开始日期
	 */
	private Date planStartDate;
	/**
	 * 相关计划编号
	 */
	private String planAssociatedPlanId;
	/**
	 * 负责人
	 */
	private String planPrincipal;
	/**
	 * 执行人
	 */
	private String planExecutor;
	/**
	 * 关联指标编号
	 */
	private Integer planAssociatedIndicatorsId;
	/**
	 * 任务送审时间
	 */
	private Date planSubmissionTime;
	/**
	 * 审批结束时间
	 */
	private Date approvalEndTime;
	/**
	 * 审批人
	 */
	private String approver;
	/**
	 * 送审人
	 */
	private String submitter;
	/**
	 * 审批状态
	 */
	private ApprovalStatus approvalStatus;
	/**
	 * 审批意见
	 */
	private String approvalComments;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 
	 */
	private String approvalContent;

}
