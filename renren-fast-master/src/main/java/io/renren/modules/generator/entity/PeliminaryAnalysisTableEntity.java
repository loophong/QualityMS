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
 * @date 2024-11-20 15:20:17
 */
@Data
@TableName("peliminary_Analysis_table")
public class PeliminaryAnalysisTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer analysisId;
	/**
	 * 初步分析
	 */
	private String peliminaryAnalysis;

}
