package io.renren.modules.taskmanagement.dto;

import io.renren.modules.taskmanagement.entity.TaskEntity;
import lombok.Data;

import java.util.List;

@Data
public class TaskUpdateDTO {
    private TaskEntity parentTask;

    private List<TaskEntity> childTask;
}
