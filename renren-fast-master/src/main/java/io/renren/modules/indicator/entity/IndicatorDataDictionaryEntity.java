package io.renren.modules.indicator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author lin
 * @email ${email}
 * @date 2024-08-14 16:51:50
 */
@Data
@TableName("indicator_data_dictionary")
public class IndicatorDataDictionaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据ID
	 */
	@TableId
	private Integer dataId;
	/**
	 * 数据
	 */
	private String dataContent;
	/**
	 * 数据类型
	 */
	private String dataType;

}
