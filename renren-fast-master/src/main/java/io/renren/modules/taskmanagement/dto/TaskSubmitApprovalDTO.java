package io.renren.modules.taskmanagement.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.modules.taskmanagement.entity.FileEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskSubmitApprovalDTO {
    private String taskId;
    private String approvalor;
    private String approvalContent;
    /**
     * 现状
     */
    private String currentStatus;
    /**
     * 目标
     */
    private String objectiveGoal;
    /**
     * 主要措施
     */
    private String keyMeasuresActions;
    /**
     * 实施人
     */
    private String implementerResponsiblePerson;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date implementationStartTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date implementationEndTime;

    private List<FileEntity> files;
}
