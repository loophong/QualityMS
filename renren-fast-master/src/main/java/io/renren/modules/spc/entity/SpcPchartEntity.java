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
@TableName("spc_Pchart")
public class SpcPchartEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long spcpId;
	/**
	 * 当月第1天数据
	 */
	private Integer date1;
	/**
	 * 当月第2天数据
	 */
	private Integer date2;
	/**
	 * 当月第3天数据
	 */
	private Integer date3;
	/**
	 * 当月第4天数据
	 */
	private Integer date4;
	/**
	 * 当月第5天数据
	 */
	private Integer date5;
	/**
	 * 当月第6天数据
	 */
	private Integer date6;
	/**
	 * 当月第7天数据
	 */
	private Integer date7;
	/**
	 * 当月第8天数据
	 */
	private Integer date8;
	/**
	 * 当月第9天数据
	 */
	private Integer date9;
	/**
	 * 当月第10天数据
	 */
	private Integer date10;
	/**
	 * 当月第11天数据
	 */
	private Integer date11;
	/**
	 * 当月第12天数据
	 */
	private Integer date12;
	/**
	 * 当月第13天数据
	 */
	private Integer date13;
	/**
	 * 当月第14天数据
	 */
	private Integer date14;
	/**
	 * 当月第15天数据
	 */
	private Integer date15;
	/**
	 * 当月第16天数据
	 */
	private Integer date16;
	/**
	 * 当月第17天数据
	 */
	private Integer date17;
	/**
	 * 当月第18天数据
	 */
	private Integer date18;
	/**
	 * 当月第19天数据
	 */
	private Integer date19;
	/**
	 * 当月第20天数据
	 */
	private Integer date20;
	/**
	 * 当月第21天数据
	 */
	private Integer date21;
	/**
	 * 当月第22天数据
	 */
	private Integer date22;
	/**
	 * 当月第23天数据
	 */
	private Integer date23;
	/**
	 * 当月第24天数据
	 */
	private Integer date24;
	/**
	 * 当月第25天数据
	 */
	private Integer date25;
	/**
	 * 当月第26天数据
	 */
	private Integer date26;
	/**
	 * 当月第27天数据
	 */
	private Integer date27;
	/**
	 * 当月第28天数据
	 */
	private Integer date28;
	/**
	 * 当月第29天数据
	 */
	private Integer date29;
	/**
	 * 当月第30天数据
	 */
	private Integer date30;
	/**
	 * 当月第31天数据
	 */
	private Integer date31;
	/**
	 * 合计
	 */
	private Integer sum;
	/**
	 * 数据日期
	 */
	private Date datatime;
	/**
	 * 数据内容（抽检数/不良数）
	 */
	private String dataContent;
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
