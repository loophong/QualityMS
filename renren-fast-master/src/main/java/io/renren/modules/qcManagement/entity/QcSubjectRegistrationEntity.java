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
	private Integer topicActivityResult;
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
	 * QC知识库
	 */
	private Integer qcExamineId;
	/**
	 * 审核关联课题
	 */
	private Integer qcExamineSubject;
	/**
	 * 审核进行步骤
	 */
	private String qcExamineCurrent;
	/**
	 * 课题提交状态
	 */
	private String qcStatusOne;
	/**
	 * 成果认定状态
	 */
	private String qcStatusTwo;
	/**
	 * 相关方审核状态
	 */
	private String qcStatusThree;
	/**
	 * 成果初评状态
	 */
	private String qcStatusFour;
	/**
	 * 成果初评总分
	 */
	private String qcFirstTotal;
	/**
	 * 成果初评等级
	 */
	private String qcFirstLevel;
	/**
	 * 成果复评状态
	 */
	private String qcStatusFive;
	/**
	 * 成果复评总分
	 */
	private String qcSecondTotal;
	/**
	 * 成果复评等级
	 */
	private String qcSecondLevel;
	/**
	 * 财务部审核状态
	 */
	private String qcStatusSix;
	/**
	 * 终评提交状态
	 */
	private String qcStatusSeven;
	/**
	 *
	 */
	private String qcSevenContent;
	/**
	 *
	 */
	private String qcSixContent;
	/**
	 *
	 */
	private String qcFiveContent;
	/**
	 * 成果复评分数
	 */
	private String qcSecondScore;
	/**
	 *
	 */
	private String qcFourContent;
	/**
	 * 成果初评分数
	 */
	private String qcFirstScore;
	/**
	 *
	 */
	private String qcThreeContent;
	/**
	 *
	 */
	private String qcTwoContent;
	/**
	 * 成果认定审核意见
	 */
	private String qcOneContent;

}
