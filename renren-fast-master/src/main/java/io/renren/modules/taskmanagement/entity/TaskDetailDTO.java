package io.renren.modules.taskmanagement.entity;

import lombok.Data;

import java.util.List;

@Data
public class TaskDetailDTO {

    private TaskEntity rootTask;

    private List<TaskEntity> tasks;

}
