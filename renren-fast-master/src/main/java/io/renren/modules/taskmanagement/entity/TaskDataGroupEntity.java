package io.renren.modules.taskmanagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-18 15:11:00
 */
@Data
@TableName("task_data_group")
public class TaskDataGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 组id
	 */
	@TableId
	private Integer tdGid;
	/**
	 * 组名
	 */
	private String groupName;
	/**
	 * 人数
	 */
	private Integer groupNumber;
	/**
	 * 描述
	 */
	private String groupDescription;

}
