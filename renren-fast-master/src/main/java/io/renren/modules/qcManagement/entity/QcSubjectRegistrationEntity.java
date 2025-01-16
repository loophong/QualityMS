package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
@Data
@TableName("qc_subject_registration")
public class QcSubjectRegistrationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id(主键)
	 */
	@TableId
	private Long qcsrId;
	/**
	 * 课题名称
	 */
	private String topicName;
	/**
	 * 课题编号
	 */
	private String topicNumber;
	/**
	 * 课题组长
	 */
	private String topicLeader;
	/**
	 * 课题顾问
	 */
	private String topicConsultant;
	/**
	 * 小组成员ids
	 */
	private String teamNumberIds;
	/**
	 * 创建日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 修改日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date modificationDate;
	/**
	 * 修改人
	 */
	private String modifier;
	/**
	 * 开始日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**
	 * 结束日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**
	 * 课题审核状态
	 */
	private short topicReviewStatus;
	/**
	 * 课题描述/摘要
	 */
	private String topicDescription;
	/**
	 * 课题类型
	 */
	private String topicType;
	/**
	 * 活动特性
	 */
	private String activityCharacteristics;
	/**
	 * 活动计划(细分日期)
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date activityPlan;
	/**
	 * 活动计划结束日期(细分日期)
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date activityPlanEnd;
	/**
	 * 课题关键字tag
	 */
	private String keywords;
	/**
	 * 课题活动状态
	 */
	private String topicActivityStatus;
	/**
	 * 课题活动评分结果
	 */
	private String topicActivityResult;
	/**
	 * 删除标记位
	 */
	private Integer deleteFlag;
	/**
	 * 备注
	 */
	private String note;
	/**
	 * 提交类型
	 */
	private String resultType;
	/**
	 * 关联审核状态id
	 */
	private String examineId;
	/**
	 * 小组名称
	 */
	private String groupName;
	/**
	 * 科室领导初审意见
	 */
	private String firstComment;
	/**
	 *组内管理员审核意见
	 */
	private String secondComment;
	/**
	 * 科室领导审核
	 */
	private String topicReviewDepartment;
	/**
	 * 组内管理员审核
	 */
	private String topicReviewGroup;
	/**
	 * 单位
	 */
	private String topicDepartment;
	/**
	 * 初评人员
	 */
	private String firstExaminePeople;
	/**
	 * 复评人员
	 */
	private String secondExaminePeople;

}
