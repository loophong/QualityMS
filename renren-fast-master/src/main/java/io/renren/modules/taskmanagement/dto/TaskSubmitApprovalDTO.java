package io.renren.modules.taskmanagement.dto;

import io.renren.modules.taskmanagement.entity.FileEntity;
import lombok.Data;

import java.util.List;

@Data
public class TaskSubmitApprovalDTO {
    private String taskId;
    private String approvalor;
    private String approvalContent;
    private List<FileEntity> files;
}
