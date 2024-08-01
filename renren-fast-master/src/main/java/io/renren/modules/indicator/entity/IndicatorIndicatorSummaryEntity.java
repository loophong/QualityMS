package io.renren.modules.indicator.entity;

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
	private Integer indicatorId;
	/**
	 * 指标名称
	 */
	@ExcelProperty(value = "指标名称")
	private String indicatorName;
	/**
	 * 指标值
	 */
	@ExcelProperty(value = "指标值")
	private BigDecimal indicatorValue;
	/**
	 * 指标值上界
	 */
	private BigDecimal indicatorValueUpperBound;
	/**
	 * 指标值下界
	 */
	private BigDecimal indicatorValueLowerBound;
	/**
	 * 考核部门
	 */
	@ExcelProperty(value = "考核部门")
	private String assessmentDepartment;
	/**
	 * 管理部门
	 */
	@ExcelProperty(value = "管理部门")
	private String managementDepartment;
	/**
	 * 指标定义
	 */
	@ExcelProperty(value = "指标定义")
	private String indicatorDefinition;
	/**
	 * 指标分级
	 */
	@ExcelProperty(value = "指标分级")
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
	@ExcelProperty(value = "收集频次")
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
	 * 年月
	 */
	@JsonFormat(pattern = "yyyy-MM")
	@TableField("`year_month`")
	private Date yearMonth;
	/**
	/**
	 * 0表示弃用，1表示使用中
	 */
	private Integer indicatorState;
	/**
	 * 指标子节点
	 */
	private String indicatorChildNode;

	public Integer getIndicatorId() {
		return indicatorId;
	}


	public void setIndicatorId(Integer indicatorId) {
		this.indicatorId = indicatorId;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public BigDecimal getIndicatorValue() {
		return indicatorValue;
	}

	public void setIndicatorValue(BigDecimal indicatorValue) {
		this.indicatorValue = indicatorValue;
	}

	public String getAssessmentDepartment() {
		return assessmentDepartment;
	}

	public void setAssessmentDepartment(String assessmentDepartment) {
		this.assessmentDepartment = assessmentDepartment;
	}

	public String getManagementDepartment() {
		return managementDepartment;
	}

	public void setManagementDepartment(String managementDepartment) {
		this.managementDepartment = managementDepartment;
	}

	public String getIndicatorDefinition() {
		return indicatorDefinition;
	}

	public void setIndicatorDefinition(String indicatorDefinition) {
		this.indicatorDefinition = indicatorDefinition;
	}

	public String getIndicatorClassification() {
		return indicatorClassification;
	}

	public void setIndicatorClassification(String indicatorClassification) {
		this.indicatorClassification = indicatorClassification;
	}

	public String getManagementContentCurrentAnalysis() {
		return managementContentCurrentAnalysis;
	}

	public void setManagementContentCurrentAnalysis(String managementContentCurrentAnalysis) {
		this.managementContentCurrentAnalysis = managementContentCurrentAnalysis;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getSourceDepartment() {
		return sourceDepartment;
	}

	public void setSourceDepartment(String sourceDepartment) {
		this.sourceDepartment = sourceDepartment;
	}

	public String getCollectionMethod() {
		return collectionMethod;
	}

	public void setCollectionMethod(String collectionMethod) {
		this.collectionMethod = collectionMethod;
	}

	public String getCollectionFrequency() {
		return collectionFrequency;
	}

	public void setCollectionFrequency(String collectionFrequency) {
		this.collectionFrequency = collectionFrequency;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getIndicatorParentNode() {
		return indicatorParentNode;
	}

	public BigDecimal getIndicatorValueUpperBound() {
		return indicatorValueUpperBound;
	}

	public void setIndicatorValueUpperBound(BigDecimal indicatorValueUpperBound) {
		this.indicatorValueUpperBound = indicatorValueUpperBound;
	}

	public BigDecimal getIndicatorValueLowerBound() {
		return indicatorValueLowerBound;
	}

	public void setIndicatorValueLowerBound(BigDecimal indicatorValueLowerBound) {
		this.indicatorValueLowerBound = indicatorValueLowerBound;
	}

	public Date getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Date yearMonth) {
		this.yearMonth = yearMonth;
	}

	public void setIndicatorParentNode(String indicatorParentNode) {
		this.indicatorParentNode = indicatorParentNode;
	}

	public Date getIndicatorCreatTime() {
		return indicatorCreatTime;
	}

	public void setIndicatorCreatTime(Date indicatorCreatTime) {
		this.indicatorCreatTime = indicatorCreatTime;
	}

	public Integer getIndicatorState() {
		return indicatorState;
	}

	public void setIndicatorState(Integer indicatorState) {
		this.indicatorState = indicatorState;
	}

	public String getIndicatorChildNode() {
		return indicatorChildNode;
	}

	public void setIndicatorChildNode(String indicatorChildNode) {
		this.indicatorChildNode = indicatorChildNode;
	}

}
