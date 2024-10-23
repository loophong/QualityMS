package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-25 14:45:45
 */
@Data
@TableName("qc_step")
public class QcStepEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 步骤主键id
	 */
	@TableId
	private Integer stepId;
	/**
	 * 关联课题id
	 */
	private Integer stepSubjectId;
	/**
	 * 课题类型
	 */
	private String stepType;
	/**
	 * 步骤id
	 */
	private Integer stepProcess;
	/**
	 * 阶段名称
	 */
	private String stageName;
	/**
	 * 计划开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date stagePlanStart;
	/**
	 * 计划结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date stagePlanEnd;
	/**
	 * 实际开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date stageActualStart;
	/**
	 * 实际结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date stageActualEnd;
	/**
	 * 参与人员
	 */

	private String stagePeople;
	/**
	 * 步骤描述
	 */
	private String stageDescribe;
	/**
	 * 备注
	 */
	private String stageExtra;
	/**
	 * 活动前现状
	 */
	private String stageBefore;
	/**
	 * 活动后现状
	 */
	private String stageAfter;
	/**
	 * 巩固措施
	 */
	private String stageConsolidate;

	/**
	 * 步骤附件
	 */
	private String stageAttachment;

}
