package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.renren.modules.taskmanagement.utils.JacksonListTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import javafx.concurrent.Task;
import lombok.Data;

/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Data
@TableName("task_management_plan")
public class PlanEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long tmPid;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 计划名
     */
    private String planName;
    /**
     * 任务内容
     */
    private String planContent;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planStartDate;
    /**
     * 计划完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planScheduleCompletionDate;
    /**
     * 计划天数
     */
    private Long planScheduleDays;
    /**
     * 实际完成日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planActualCompletionDate;
    /**
     * 实际天数
     */
    private Long planActualDays;
    /**
     * 任务派发
     */
    private String planTasksAssignment;
    /**
     * 关联任务编号
     */
    private String planAssociatedTasksId;
    /**
     * 进度
     */
    private String planSchedule;
    /**
     * 负责人
     */
    private String planPrincipal;
    /**
     * 执行人
     */

//	@TypeHandler(JacksonListTypeHandler.class)
//	private List<String> planExecutor;
	@TableField(typeHandler = JacksonListTypeHandler.class)
    private List<String> planExecutor;

    /**
     * 审核人
     */
    private String planAuditor;
    /**
     * 当前状态
     */
    private Integer planCurrentState;
    /**
     * 是否完成
     */
    private Integer planIsCompleted;
    /**
     * 是否超期
     */
    private Integer planIsOverdue;
    /**
     * 按时完工
     */
    private Integer planIsOnTime;
    /**
     * 提前完工
     */
    private Long planEarlyCompletionDays;
    /**
     * 滞后天数
     */
    private Long planLagDays;
    /**
     * 滞后原因
     */
    private String planLagReasons;
    /**
     * 关联指标编号
     */
    private String planAssociatedIndicatorsId;

//	@TableField(typeHandler = JacksonTypeHandler.class)
//	private List<TaskEntity> tasks;

}
