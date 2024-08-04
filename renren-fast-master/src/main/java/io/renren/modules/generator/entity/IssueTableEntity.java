package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
@Data
@TableName("issue_table")
public class IssueTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer issueId;
	/**
	 * 序号
	 */
	private String serialNumber;
	/**
	 * 问题编号
	 */
	private String issueNumber;
	/**
	 * 指标ID
	 */
	private String indicatorId;
	/**
	 * 检查科室
	 */
	private String inspectionDepartment;
	/**
	 * 检查日期
	 */
	private Date inspectionDate;
	/**
	 * 问题类别ID
	 */
	private String issueCategoryId;
	/**
	 * 车型ID
	 */
	private String vehicleTypeId;
	/**
	 * 车号ID
	 */
	private String vehicleNumberId;
	/**
	 * 问题描述
	 */
	private String issueDescription;
	/**
	 * 问题照片
	 */
	private String issuePhoto;
	/**
	 * 整改要求
	 */
	private String rectificationRequirement;
	/**
	 * 要求完成时间
	 */
	private Date requiredCompletionTime;
	/**
	 * 责任科室
	 */
	private String responsibleDepartment;
	/**
	 * 整改情况
	 */
	private String rectificationStatus;
	/**
	 * 实际完成时间
	 */
	private Date actualCompletionTime;
	/**
	 * 整改照片交付物
	 */
	private String rectificationPhotoDeliverable;
	/**
	 * 整改责任人
	 */
	private String rectificationResponsiblePerson;
	/**
	 * 要求二次整改时间
	 */
	private Date requiredSecondRectificationTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private Date creationTime;
	/**
	 * 最后修改人
	 */
	private String lastModifier;
	/**
	 * 最后修改时间
	 */
	private Date lastModificationTime;
	/**
	 * 关联问题整改记录
	 */
	private String associatedRectificationRecords;
	/**
	 * 关联问题添加
	 */
	private String associatedIssueAddition;
	/**
	 * 创建时长
	 */
	private String creationDuration;
	/**
	 * 原因分析
	 */
	private String causeAnalysis;
	/**
	 * 整改验证情况
	 */
	private String rectificationVerificationStatus;
	/**
	 * 验证结论
	 */
	private String verificationConclusion;
	/**
	 * 验证人
	 */
	private String verifier;
	/**
	 * 审核人
	 */
	private String reviewers;
	/**
	 * 问题等级
	 */
	private String level;
	/**
	 * 问题状态
	 */
	private String state;
	/**
	 * 公式
	 */
	private String formula;

}