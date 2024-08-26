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
 * @date 2024-07-21 18:47:20
 */
@Data
@TableName("indicator_key_indicators")
public class IndicatorKeyIndicatorsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 重点指标ID（主键）
	 */
	@TableId
	private Integer keyIndicatorId;
	/**
	 * 指标ID
	 */
	private String indicatorId;
	/**
	 * 指标名称
	 */
	private String indicatorName;
	/**
	 * 指标分级
	 */
	private String indicatorClassification;
	/**
	 * 考核部门
	 */
	private String assessmentDepartment;
	/**
	 * 来源部门
	 */
	private String sourceDepartment;
	/**
	 * 管理内容
	 */
	private String managementContent;
	/**
	 * 是否管理失控
	 */
	private String isManagementOutOfControl;
	/**
	 * 是否需要理攻关
	 */
	private String isNeedsControl;
	/**
	 * 关键要素
	 */
	private String keyElements;
	/**
	 * 潜在失效模式
	 */
	private String potentialFailureMode;
	/**
	 * 潜在失效后果
	 */
	private String potentialFailureConsequences;
	/**
	 * 涉及产品
	 */
	private String involvedProduct;
	/**
	 * 过程名称
	 */
	private String processName;
	/**
	 * 控制位置
	 */
	private String controlPosition;
	/**
	 * 控制人员
	 */
	private String controlPersonnel;
	/**
	 * 控制方法
	 */
	private String controlMethod;
	/**
	 * 评价测量技术
	 */
	private String evaluationMeasurementTechnique;
	/**
	 * 样本大小
	 */
	private String sampleSize;
	/**
	 * 抽样频率
	 */
	private String samplingFrequency;
	/**
	 * 控制清单
	 */
	private String controlList;

}
