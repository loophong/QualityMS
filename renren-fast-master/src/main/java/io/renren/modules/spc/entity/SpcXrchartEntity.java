package io.renren.modules.spc.entity;

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
 * @date 2024-11-27 10:49:53
 */
@Data
@TableName("spc_XRchart")
public class SpcXrchartEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long spcxrId;
	/**
	 * 当月第1天数据
	 */
	private Float date1;
	/**
	 * 当月第2天数据
	 */
	private Float date2;
	/**
	 * 当月第3天数据
	 */
	private Float date3;
	/**
	 * 当月第4天数据
	 */
	private Float date4;
	/**
	 * 当月第5天数据
	 */
	private Float date5;
	/**
	 * 当月第6天数据
	 */
	private Float date6;
	/**
	 * 当月第7天数据
	 */
	private Float date7;
	/**
	 * 当月第8天数据
	 */
	private Float date8;
	/**
	 * 当月第9天数据
	 */
	private Float date9;
	/**
	 * 当月第10天数据
	 */
	private Float date10;
	/**
	 * 当月第11天数据
	 */
	private Float date11;
	/**
	 * 当月第12天数据
	 */
	private Float date12;
	/**
	 * 当月第13天数据
	 */
	private Float date13;
	/**
	 * 当月第14天数据
	 */
	private Float date14;
	/**
	 * 当月第15天数据
	 */
	private Float date15;
	/**
	 * 当月第16天数据
	 */
	private Float date16;
	/**
	 * 当月第17天数据
	 */
	private Float date17;
	/**
	 * 当月第18天数据
	 */
	private Float date18;
	/**
	 * 当月第19天数据
	 */
	private Float date19;
	/**
	 * 当月第20天数据
	 */
	private Float date20;
	/**
	 * 当月第21天数据
	 */
	private Float date21;
	/**
	 * 当月第22天数据
	 */
	private Float date22;
	/**
	 * 当月第23天数据
	 */
	private Float date23;
	/**
	 * 当月第24天数据
	 */
	private Float date24;
	/**
	 * 当月第25天数据
	 */
	private Float date25;
	/**
	 * 当月第26天数据
	 */
	private Float date26;
	/**
	 * 当月第27天数据
	 */
	private Float date27;
	/**
	 * 当月第28天数据
	 */
	private Float date28;
	/**
	 * 当月第29天数据
	 */
	private Float date29;
	/**
	 * 当月第30天数据
	 */
	private Float date30;
	/**
	 * 当月第31天数据
	 */
	private Float date31;
	/**
	 * 数据日期
	 */
	private Date datatime;
	/**
	 * 标准上限USL
	 */
	private Float upperLimitStandards;
	/**
	 * 标准中心限CL
	 */
	private Float centerLimitStandards;
	/**
	 * 标准下限LSL
	 */
	private Float lowerLimitStandards;
	/**
	 * X上限UCL
	 */
	private Float upperLimitX;
	/**
	 * X中心限CL
	 */
	private Float centerLimitX;
	/**
	 * X下限LCL
	 */
	private Float lowerLimitX;
	/**
	 * R上限UCL
	 */
	private Float upperLimitR;
	/**
	 * R中心限CL
	 */
	private Float centerLimitR;
	/**
	 * R下限LCL
	 */
	private Float lowerLimitR;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备用字段1
	 */
	private String alternateFields1;
	/**
	 * 备用字段2
	 */
	private Integer alternateFields2;
	/**
	 * 备用字段3
	 */
	private Float alternateFields3;
	/**
	 * 备用字段4
	 */
	private Date alternateFields4;
	/**
	 * 逻辑删除标志
	 */
	private Integer deleteFlag;

}
