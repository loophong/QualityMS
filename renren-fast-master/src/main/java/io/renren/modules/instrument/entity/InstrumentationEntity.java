package io.renren.modules.instrument.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
	@ExcelProperty(index = 0)
	private String warehouseNumber;
	/**
	 * 量具编号
	 */
	@ExcelProperty(index = 1)
	private String toolNumber;
	/**
	 * 量具名称
	 */
	@ExcelProperty(index = 2)
	private String toolName;
	/**
	 * 规格型号
	 */
	@ExcelProperty(index = 3)
	private String specificationModel;
	/**
	 * 量程
	 */
	@ExcelProperty(index = 4)
	private String rangeValue;
	/**
	 * 分度值
	 */
	@ExcelProperty(index = 5)
	private String divisionValue;
	/**
	 * 精度
	 */
	@ExcelProperty(index = 6)
	private String accuracy;
	/**
	 * 用途
	 */
	@ExcelProperty(index = 7)
	private String purpose;
	/**
	 * 供应商
	 */
	@ExcelProperty(index = 8)
	private String supplier;
	/**
	 * 出厂编号
	 */
	@ExcelProperty(index = 9)
	private String factoryNumber;
	/**
	 * 公司编号
	 */
	@ExcelProperty(index = 10)
	private String companyNumber;
	/**
	 * 级别
	 */
	@ExcelProperty(index = 11)
	private String level;
	/**
	 * 检定周期
	 */
	@ExcelProperty(index = 12)
	private Integer verificationCycle;
	/**
	 * 检定状态
	 */
	@ExcelProperty(index = 13)
	private String verificationStatus;
	/**
	 * 使用状态
	 */
	@ExcelProperty(index = 14)
	private String usageStatus;
	/**
	 * 配置地点
	 */
	@ExcelProperty(index = 15)
	private String allocationLocation;
	/**
	 * 责任部门
	 */
	@ExcelProperty(index = 16)
	private String responsibleDepartment;
	/**
	 * 使用人
	 */
	@ExcelProperty(index = 17)
	private String userName;
	/**
	 * 检校日期
	 */
	@ExcelProperty(index = 18)
	private String calibrationDate;
	/**
	 * 检校有效期
	 */
	@ExcelProperty(index = 19)
	private String calibrationValidity;
	/**
	 * 检校单位
	 */
	@ExcelProperty(index = 20)
	private String calibrationUnit;
	/**
	 * 证书编号
	 */
	@ExcelProperty(index = 21)
	private String certificateNumber;
	/**
	 * 备注
	 */
	@ExcelProperty(index = 22)
	private String remarks;
	/**
	 * 数量
	 */
	@ExcelProperty(index = 23)
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
