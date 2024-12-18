package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-10 15:50:58
 */
@Data
@TableName("task_management_approval_file")
public class ApprovalFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     *
     */
    private Long approvalId;
    /**
     *
     */
    private String taskId;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String url;

}
