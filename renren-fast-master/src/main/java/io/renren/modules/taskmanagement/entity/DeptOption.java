package io.renren.modules.taskmanagement.entity;

import lombok.Data;

/**
 * @BelongsProject: QualityMS
 * @BelongsPackage: io.renren.modules.taskmanagement.entity
 * @Author: hong
 * @CreateTime: 2024-07-23  15:11
 * @Description: TODO
 * @Version: 1.0
 */

@Data
public class DeptOption {
    private String value;
    private String label;

    public DeptOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

}