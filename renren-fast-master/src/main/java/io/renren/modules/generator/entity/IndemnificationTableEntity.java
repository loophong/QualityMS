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
 * @date 2025-01-10 15:53:47
 */
@Data
@TableName("Indemnification_table")
public class IndemnificationTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer indeId;
	/**
	 * 赔偿件
	 */
	private String indemnification;
	/**
	 * 图号
	 */
	private String figureNumber;
	/**
	 * 质量成本
	 */
	private Double qualitycost;

}
