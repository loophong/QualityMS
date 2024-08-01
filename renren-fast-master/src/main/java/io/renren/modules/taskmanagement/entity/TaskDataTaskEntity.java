package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务管理模块任务表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-18 16:27:33
 */
@Data
@TableName("task_data_task")
public class TaskDataTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务ID
	 */
	@TableId
	private Long taskId;
	/**
	 * 任务内容
	 */
	private String taskContent;
	/**
	 * 开始日期
	 */
	private Date startDate;
	/**
	 * 计划完成日期
	 */
	private Date plannedFinishDate;
	/**
	 * 计划天数
	 */
	private Long scheduleDays;
	/**
	 * 实际完成日期
	 */
	private Date actualFinishingDate;
	/**
	 * 实际天数
	 */
	private Long actualDays;
	/**
	 * 相关计划编号
	 */
	private String relevantProjectNumber;
	/**
	 * 进度
	 */
	private String schedule;
	/**
	 * 负责人
	 */
	private String principal;
	/**
	 * 执行人
	 */
	private String executor;
	/**
	 * 审核人
	 */
	private String auditor;
	/**
	 * 当前状态
	 */
	private String currentState;
	/**
	 * 是否完成
	 */
	private Long whetherComplete;
	/**
	 * 是否超期
	 */
	private Long whetherOverdue;
	/**
	 * 按时完工
	 */
	private Long finishOnTime;
	/**
	 * 提前完工
	 */
	private Long daysAheadOfSchedule;
	/**
	 * 滞后天数
	 */
	private Long lagDays;
	/**
	 * 滞后原因
	 */
	private String delayReasons;
	/**
	 * 关联指标编号
	 */
	private String associatedIndexNumber;

}
