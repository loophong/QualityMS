package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author lin
 * @email ${email}
 * @date 2024-08-27 14:22:12
 */
@Data
@TableName("qc_examine_status")
public class QcExamineStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
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
	/**
	 * 入库标识
	 */
	private String qcSevenAvg;
	/**
	 * 入库标识
	 */
	private String qcSevenLevel;
	/**
	 * 入库标识
	 */
	private String qcStorageFlag;
	/**
	 * 初评综合
	 */
	private String qcFirstAll;
	/**
	 * 复评综合
	 */
	private String qcSecondAll;

}
