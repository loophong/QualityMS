package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 任务管理模块任务表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Data
@TableName("task_management_task")
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long tmTid;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 任务ID
	 */
	private String taskName;
	/**
	 * 任务内容
	 */
	private String taskContent;
	/**
	 * 开始日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date taskStartDate;
	/**
	 * 计划完成日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date taskScheduleCompletionDate;
	/**
	 * 计划天数
	 */
	private Long taskScheduleDays;
	/**
	 * 实际完成日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date taskActualCompletionDate;
	/**
	 * 实际天数
	 */
	private Long taskActualDays;
	/**
	 * 相关计划编号
	 */
	private String taskAssociatedPlanId;
	/**
	 * 进度
	 */
	private String taskSchedule;
	/**
	 * 负责人
	 */
	private String taskPrincipal;
	/**
	 * 执行人
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<String> taskExecutor;
	/**
	 * 审核人
	 */
	private String taskAuditor;
	/**
	 * 当前状态
	 */
	private TaskStatus taskCurrentState;
	/**
	 * 是否完成
	 */
	private Long taskIsCompleted;
	/**
	 * 是否超期
	 */
	private Long taskIsOverdue;
	/**
	 * 按时完工
	 */
	private Long taskIsOnTime;
	/**
	 * 提前完工
	 */
	private Long taskEarlyCompletionDays;
	/**
	 * 滞后天数
	 */
	private Long taskLagDays;
	/**
	 * 滞后原因
	 */
	private String taskLagReasons;
	/**
	 * 关联指标编号
	 */
	private String taskAssociatedIndicatorsId;

}
