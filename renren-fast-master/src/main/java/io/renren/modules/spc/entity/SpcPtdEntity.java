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
 * @date 2024-12-03 15:22:22
 */
@Data
@TableName("spc_PTD")
public class SpcPtdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 接收区域
	 */
	private Double acceptanceRegion;
	/**
	 * 工时
	 */
	private Double workTime;
	/**
	 * 正态分布值,按照公式计算
	 */
	private Double ptdValue;
	/**
	 * 频率
	 */
	private Integer frequency;
	/**
	 * 创建时间
	 */
	private Date craetTime;
	/**
	 * 创建人
	 */
	private String createPeople;
	/**
	 * 逻辑删除字段
	 */
	private String deletFlag;
	/**
	 * 备用字段
	 */
	private String spare;
	/**
	 * 数据导入日期
	 */
	private Date dataImportTime;
	/**
	 * 数据更新时间
	 */
	private Date updateTime;

}
