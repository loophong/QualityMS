package io.renren.modules.taskmanagement.dto;

import io.renren.modules.taskmanagement.entity.FileEntity;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import lombok.Data;

import java.util.List;

@Data
public class PlanDTO {
    private PlanEntity plan;

    private List<TaskEntity> tasks;

    private List<FileEntity> files;
}
