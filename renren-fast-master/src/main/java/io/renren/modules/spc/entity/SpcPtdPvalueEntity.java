package io.renren.modules.spc.entity;

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
 * @date 2024-12-19 16:13:33
 */
@Data
@TableName("spc_PTD_Pvalue")
public class SpcPtdPvalueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long pvalueId;
	/**
	 * 表格b列值
	 */
	private Integer bValue;
	/**
	 * 表格c列值
	 */
	private Integer cValue;
	/**
	 * 表格d列值
	 */
	private Integer dValue;
	/**
	 * 数据所在的表格名称
	 */
	private String tableName;
	/**
	 * 数据日期
	 */
	private Date datatime;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备用字段1
	 */
	private String alternateFields1;
	/**
	 * 备用字段2
	 */
	private Integer alternateFields2;
	/**
	 * 备用字段3
	 */
	private Float alternateFields3;
	/**
	 * 备用字段4
	 */
	private Date alternateFields4;
	/**
	 * 逻辑删除标志
	 */
	private Integer deleteFlag;

}
