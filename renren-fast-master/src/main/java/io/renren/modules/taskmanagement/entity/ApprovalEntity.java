package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务审批表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-08-23 09:58:46
 */
@Data
@TableName("task_management_approval_table")
public class ApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 审批编号
	 */
	@TableId
	private Long approvalId;
	/**
	 * 任务ID
	 */
	private Long taskId;
	/**
	 * 任务名
	 */
	private String taskName;
	/**
	 * 任务内容
	 */
	private String taskContent;
	/**
	 * 开始日期
	 */
	private Date taskStartDate;
	/**
	 * 相关计划编号
	 */
	private Integer taskAssociatedPlanId;
	/**
	 * 负责人
	 */
	private String taskPrincipal;
	/**
	 * 执行人
	 */
	private String taskExecutor;
	/**
	 * 关联指标编号
	 */
	private Integer taskAssociatedIndicatorsId;
	/**
	 * 任务送审时间
	 */
	private Date taskSubmissionTime;
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

}
