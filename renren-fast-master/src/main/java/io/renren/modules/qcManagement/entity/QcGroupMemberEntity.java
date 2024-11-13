package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
@Data
@TableName("qc_group_members")

public class QcGroupMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 组员ID（主键）
	 */
	@TableId
	private Long qcgmId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 手机号
	 */
	private String telNumber;
	/**
	 * 员工编号
	 */
	private String number;
	/**
	 * 角色id
	 */
	private Long memberRole;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 单位
	 */
	private String department;
	/**
	 * 岗位
	 */
	private String position;
	/**
	 * 小组类型
	 */
	private String team;
	/**
	 * 参加QC时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date participationDate;
	/**
	 * QC课题
	 */
	private String topic;
	/**
	 * QC课题内角色
	 */
	private String roleInTopic;
	/**
	 * 逻辑删除flag
	 */
	private Integer deleteFlag;
	/**
	 * 父节点ID
	 */
	private Integer parentId;
	/**
	 * 小组名称
	 */
	private String groupName;
	/**
	 * 注册号
	 */
	private String registrationNum;
	/**
	 * 小组审核状态
	 */
	private String examineStatus;

}
