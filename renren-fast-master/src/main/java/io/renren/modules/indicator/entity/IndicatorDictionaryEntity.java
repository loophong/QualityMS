package io.renren.modules.indicator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 17:55:09
 */
@Data
@TableName("indicator_dictionary")
public class IndicatorDictionaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 指标ID
	 */
	@TableId
	private Integer indicatorId;
	/**
	 * 指标名称
	 */
	private String indicatorName;
	/**
	 * 考核部门
	 */
	private String assessmentDepartment;
	/**
	 * 管理部门
	 */
	private String managementDepartment;
	/**
	 * 指标定义
	 */
	private String indicatorDefinition;
	/**
	 * 指标分级
	 */
	private String indicatorClassification;
	/**
	 * 管理内容（现状分析）
	 */
	private String managementContentCurrentAnalysis;
	/**
	 * 数据ID
	 */
	private Integer dataId;
	/**
	 * 来源部门
	 */
	private String sourceDepartment;
	/**
	 * 收集方法
	 */
	private String collectionMethod;
	/**
	 * 收集频次
	 */
	private String collectionFrequency;
	/**
	 * 关联计划id
	 */
	private String planId;
	/**
	 * 关联任务id
	 */
	private String taskId;
	/**
	 * 指标父节点
	 */
	private String indicatorParentNode;
	/**
	 * 指标创建时间
	 */
	private Date indicatorCreatTime;
	/**
	 * 0表示弃用，1表示使用中
	 */
	private Integer indicatorState;
	/**
	 * 指标子节点
	 */
	private String indicatorChildNode;

}
