package io.renren.modules.publicmanagement.entity;

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
	private String companyId;
	/**
	 * 级别
	 */
	private String level;
	/**
	 * 检定周期
	 */
	private Integer calibrationCycle;
	/**
	 * 检定状态
	 */
	private String calibrationStatus;
	/**
	 * 使用状态
	 */
	private String usageStatus;
	/**
	 * 配置地点
	 */
	private String location;
	/**
	 * 责任部门
	 */
	private String responsibleDepartment;
	/**
	 * 使用人
	 */
	private String user;
	/**
	 * 检校日期
	 */
	private String calibrationDate;
	/**
	 * 检校有效期
	 */
	private String calibrationValidity;
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
	private String remark;
	/**
	 * 数量
	 */
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
