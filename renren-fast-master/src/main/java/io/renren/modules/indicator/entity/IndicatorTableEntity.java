package io.renren.modules.indicator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 指标表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 14:24:18
 */
@Data
@TableName("indicator_table")
public class IndicatorTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 指标名称
	 */
	private String indicatorName;
	/**
	 * 指标值
	 */
	private BigDecimal indicatorValue;
	/**
	 * 所属部门
	 */
	private String department;
	/**
	 * 指标等级
	 */
	private String indicatorLevel;
	/**
	 * 创建时间
	 */
	private Date createedTime;

}
