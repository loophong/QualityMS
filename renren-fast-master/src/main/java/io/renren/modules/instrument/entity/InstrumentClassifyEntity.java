package io.renren.modules.instrument.entity;

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
 * @date 2025-01-16 16:51:59
 */
@Data
@TableName("instrument_classify")
public class InstrumentClassifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 一级分类
	 */
	private String primaryCategory;
	/**
	 * 二级分类
	 */
	private String secondaryCategory;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 逻辑删除标识 (0: 不删除, 1: 删除)
	 */
	private Integer isDeleted;
	/**
	 * 备用字段1
	 */
	private String backupField1;
	/**
	 * 创建时间
	 */
	private Date createdAt;

}
