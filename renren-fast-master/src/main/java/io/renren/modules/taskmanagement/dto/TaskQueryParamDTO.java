package io.renren.modules.taskmanagement.dto;


import io.renren.modules.taskmanagement.entity.TaskEntity;
import lombok.Data;

@Data
public class TaskQueryParamDTO {
    Integer page;

    Integer limit;

    TaskEntity task;
}
