package io.renren.modules.publicmanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 仪器台账表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-08-02 13:37:30
 */
@Data
@TableName("instrument_ledger")
public class InstrumentLedgerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	/**
	 * 公司编号
	 */
	@ExcelProperty(index = 0)
	private String companyId;
	/**
	 * 级别
	 */
	@ExcelProperty(index = 1)
	private String level;
	/**
	 * 检定周期
	 */
	@ExcelProperty(index = 2)
	private Integer calibrationCycle;
	/**
	 * 检定状态
	 */
	@ExcelProperty(index = 3)
	private String calibrationStatus;
	/**
	 * 使用状态
	 */
	@ExcelProperty(index = 4)
	private String usageStatus;
	/**
	 * 配置地点
	 */
	@ExcelProperty(index = 5)
	private String location;
	/**
	 * 责任部门
	 */
	@ExcelProperty(index = 6)
	private String responsibleDepartment;
	/**
	 * 使用人
	 */
	@ExcelProperty(index = 7)
	private String user;
	/**
	 * 检校日期
	 */
	@ExcelProperty(index = 8)
	private Date calibrationDate;
	/**
	 * 检校有效期
	 */
	@ExcelProperty(index = 9)
	private Date calibrationValidity;
	/**
	 * 检校单位
	 */
	@ExcelProperty(index = 10)
	private String calibrationUnit;
	/**
	 * 证书编号
	 */
	@ExcelProperty(index = 11)
	private String certificateNumber;
	/**
	 * 备注
	 */
	@ExcelProperty(index = 12)
	private String remark;
	/**
	 * 数量
	 */
	@ExcelProperty(index = 13)
	private Integer quantity;
	/**
	 * 逻辑删除字段
	 */
	private Integer flag;
	/**
	 * 创建日期
	 */
	private String creatTime;

}
