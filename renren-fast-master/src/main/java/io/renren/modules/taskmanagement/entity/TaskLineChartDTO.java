package io.renren.modules.taskmanagement.entity;

import lombok.Data;

@Data
public class TaskLineChartDTO {
    private String taskId;

    private Integer taskScheduleDays;

    private Integer taskActualDays;
}
