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
 * @date 2024-11-20 15:20:56
 */
@Data
@TableName("issue_fault_table")
public class IssueFaultTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Integer faultId;
	/**
	 * 系统分类
	 */
	private String systematicClassification;
	/**
	 * 故障件一级
	 */
	private String firstFaultyParts;
	/**
	 * 故障件二级
	 */
	private String secondFaultyParts;
	/**
	 * 故障类别
	 */
	private String faultType;
	/**
	 * 故障模式
	 */
	private String faultModel;

}
