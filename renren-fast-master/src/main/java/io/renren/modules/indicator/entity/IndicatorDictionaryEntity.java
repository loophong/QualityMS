package io.renren.modules.indicator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date indicatorCreatTime;

	/**
	 * 0表示弃用，1表示使用中
	 */
	private Integer indicatorState;
	/**
	 * 指标子节点
	 */
	private String indicatorChildNode;

	/**
	 * 指标值上界
	 */
	private Integer indicatorValueUpperBound;
	/**
	 * 指标值下界
	 */
	private Integer indicatorValueLowerBound;

	/**
	* 入库标识符
	*/
	private Integer storageFlag;

	/**
	 * 权重
	 */
	private Float weight;

	/**
	 * 指标目标值
	 */
	private BigDecimal indicatorPlannedValue;

	/**
	 * 指标目标值边界标识符（0表示下边界，1表示上边界）
	 */
	private Integer indicatorBoundFlag;

}
