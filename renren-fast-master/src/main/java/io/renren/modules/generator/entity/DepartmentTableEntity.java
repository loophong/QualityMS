package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-11-11 15:25:55
 */
@Data
@TableName("department_table")
public class DepartmentTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Integer departmentId;
	/**
	 * 部门编号
	 */
	private Long departmentNumber;
	/**
	 * 部门名称
	 */
	private String departmentName;

}
