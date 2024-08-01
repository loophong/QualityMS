package io.renren.modules.taskmanagement.entity;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: QualityMS
 * @BelongsPackage: io.renren.modules.taskmanagement.entity
 * @Author: hong
 * @CreateTime: 2024-07-23  15:10
 * @Description: TODO
 * @Version: 1.0
 */

@Data
public class DeptDTO {
    private String label;
    private List<DeptOption> options;

    public DeptDTO(String label, List<DeptOption> options) {
        this.label = label;
        this.options = options;
    }

    // Getters and Setters
}


