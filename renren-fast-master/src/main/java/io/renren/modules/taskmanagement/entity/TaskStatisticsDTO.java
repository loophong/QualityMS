package io.renren.modules.taskmanagement.entity;

import lombok.Data;

import java.util.List;

@Data
public class TaskStatisticsDTO {

    // 任务总数
    private Integer taskTotal;

    // 已完成任务数
    private Integer taskCompleted;

    // 未完成任务数
    private Integer taskUncompleted;

    // 提前完成
    private Integer taskAheadOfTime;

    // 提前完成率
    private Double taskAheadOfTimeRate;

    // 滞后完成
    private Integer taskBehindSchedule;

    // 按时完成
    private Integer taskOnTime;

    // 按时完成率
    private Double taskOnTimeRate;

    // 任务列表
    private List<TaskEntity> taskList;

}
