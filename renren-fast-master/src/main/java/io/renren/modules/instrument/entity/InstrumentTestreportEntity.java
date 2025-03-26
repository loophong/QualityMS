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
@TableName("instrument_testreport")
public class InstrumentTestreportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 序号
	 */
	private String serialNumber;
	/**
	 * 检验报告类别
	 */
	private String testReportType;
	/**
	 * 检验报告名称
	 */
	private String testReportName;
	/**
	 * 检验件名称
	 */
	private String testItemName;
	/**
	 * 检验件规格型号
	 */
	private String testItemStandardNumber;
	/**
	 * 检验件批次/编号
	 */
	private String testItemBatchNumber;
	/**
	 * 测量设备
	 */
	private String measuringEquipment;
	/**
	 * 测量方法
	 */
	private String measuringMethod;
	/**
	 * 检验人
	 */
	private String tester;
	/**
	 * 检验日期
	 */
	private Date testDate;
	/**
	 * 检验报告
	 */
	private String testReport;
	/**
	 * 备注
	 */
	private String notes;
	/**
	 * 逻辑删除标识 (0: 不删除, 1: 删除)
	 */
	private Integer isDeleted;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 备用字段
	 */
	private String backupField;

}
