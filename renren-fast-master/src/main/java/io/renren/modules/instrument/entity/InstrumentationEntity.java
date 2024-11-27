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
 * @date 2024-10-31 10:36:37
 */
@Data
@TableName("Instrumentation")
public class InstrumentationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 仓库号
	 */
	private String warehouseNumber;
	/**
	 * 量具编号
	 */
	private String toolNumber;
	/**
	 * 量具名称
	 */
	private String toolName;
	/**
	 * 规格型号
	 */
	private String specificationModel;
	/**
	 * 量程
	 */
	private String rangeValue;
	/**
	 * 分度值
	 */
	private String divisionValue;
	/**
	 * 精度
	 */
	private String accuracy;
	/**
	 * 用途
	 */
	private String purpose;
	/**
	 * 供应商
	 */
	private String supplier;
	/**
	 * 出厂编号
	 */
	private String factoryNumber;
	/**
	 * 公司编号
	 */
	private String companyNumber;
	/**
	 * 级别
	 */
	private String level;
	/**
	 * 检定周期
	 */
	private Integer verificationCycle;
	/**
	 * 检定状态
	 */
	private String verificationStatus;
	/**
	 * 使用状态
	 */
	private String usageStatus;
	/**
	 * 配置地点
	 */
	private String allocationLocation;
	/**
	 * 责任部门
	 */
	private String responsibleDepartment;
	/**
	 * 使用人
	 */
	private String userName;
	/**
	 * 检校日期
	 */
	private Date calibrationDate;
	/**
	 * 检校有效期
	 */
	private Date calibrationValidity;
	/**
	 * 检校单位
	 */
	private String calibrationUnit;
	/**
	 * 证书编号
	 */
	private String certificateNumber;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 数量
	 */
	private Integer quantity;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	/**
	 * 创建人
	 */
	private String createPeople;
	/**
	 * 负责人
	 */
	private String leader;

}
