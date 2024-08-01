package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: QualityMS
 * @BelongsPackage: io.renren.modules.taskmanagement.entity
 * @Author: hong
 * @CreateTime: 2024-07-22  16:58
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class PlanAndTaskDTO {

    private PlanEntity plan;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TaskEntity> tasks;
}
