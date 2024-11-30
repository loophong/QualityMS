package io.renren.modules.indicator.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-20 14:17:04
 */
@Data
@TableName("indicator_indicator_summary")
public class IndicatorIndicatorSummaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 指标ID
	 */
	@TableId
	@ExcelIgnore
	private Integer indicatorId;

	/**
	 * 指标名称
	 */
	@ExcelProperty(value = "指标名称")
	private String indicatorName;
	/**
	 * 指标目标值
	 */
	@ExcelIgnore
	private BigDecimal indicatorValue;

	@ExcelIgnore
	private BigDecimal indicatorActualValue;
	/**
	 * 指标值上界
	 */
	@ExcelIgnore
	private BigDecimal indicatorValueUpperBound;
	/**
	 * 指标值下界
	 */
	@ExcelIgnore
	private BigDecimal indicatorValueLowerBound;
	/**
	 * 考核部门
	 */
//	@ExcelProperty(value = "考核部门")
	@ExcelIgnore
	private String assessmentDepartment;
	/**
	 * 管理部门
	 */
//	@ExcelProperty(value = "管理部门")
	@ExcelIgnore
	private String managementDepartment;
	/**
	 * 指标定义
	 */
//	@ExcelProperty(value = "指标定义")
	@ExcelIgnore
	private String indicatorDefinition;
	/**
	 * 指标分级
	 */
//	@ExcelProperty(value = "指标分级")
	@ExcelIgnore
	private String indicatorClassification;
	/**
	 * 管理内容（现状分析）
	 */
	@ExcelIgnore
	private String managementContentCurrentAnalysis;
	/**
	 * 数据ID
	 */
	@ExcelIgnore
	private Integer dataId;
	/**
	 * 来源部门
	 */
	@ExcelIgnore
	private String sourceDepartment;
	/**
	 * 收集方法
	 */
	@ExcelIgnore
	private String collectionMethod;
	/**
	 * 收集频次
	 */
//	@ExcelProperty(value = "收集频次")
	@ExcelIgnore
	private String collectionFrequency;
	/**
	 * 关联计划id
	 */
	@ExcelIgnore
	private String planId;
	/**
	 * 关联任务id
	 */
	@ExcelIgnore
	private String taskId;
	/**
	 * 指标父节点
	 */
	@ExcelIgnore
	private String indicatorParentNode;
	/**
	 * 指标创建时间
	 */
	@ExcelIgnore
	private Date indicatorCreatTime;
	/**
	 * 年月
	 */
	@JsonFormat(pattern = "yyyy-MM")
	@TableField("`year_month`")
	@ExcelIgnore
	private Date yearMonth;
	/**
	/**
	 * 0表示弃用，1表示使用中
	 */
	@ExcelIgnore
	private Integer indicatorState;
	/**
	 * 指标子节点
	 */
	@ExcelIgnore
	private String indicatorChildNode;

	/**
	 * 指指标完成情况（完成为 1，未完成为 0）
	 */
	@ExcelIgnore
	private Integer finishedFlag;


}
