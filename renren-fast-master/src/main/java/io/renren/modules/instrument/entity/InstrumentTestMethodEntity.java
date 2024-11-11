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
 * @date 2024-10-31 10:55:46
 */
@Data
@TableName("Instrument_test_method")
public class InstrumentTestMethodEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 科室
	 */
	private String department;
	/**
	 * 标准编号
	 */
	private String standardnumber;
	/**
	 * 编制
	 */
	private String compilation;
	/**
	 * 编制日期
	 */
	private Date compilationdate;
	/**
	 * 审核
	 */
	private String reviewer;
	/**
	 * 审核日期
	 */
	private Date reviewdate;
	/**
	 * 会签
	 */
	private String countersignature;
	/**
	 * 批准
	 */
	private String approval;
	/**
	 * 检验零部件
	 */
	private String inspectionparts;
	/**
	 * 图号
	 */
	private String drawingnumber;
	/**
	 * 检验件类别
	 */
	private String inspectionpartcategory;
	/**
	 * 工序名称
	 */
	private String processname;
	/**
	 * 检验类别
	 */
	private String inspectioncategory;
	/**
	 * 检验项目
	 */
	private String inspectionitem;
	/**
	 * 检验参数名称
	 */
	private String inspectionparametername;
	/**
	 * 检验部位
	 */
	private String inspectionlocation;
	/**
	 * 上限
	 */
	private String upperlimit;
	/**
	 * 下限
	 */
	private String lowerlimit;
	/**
	 * 测量仪器名称
	 */
	private String measurementinstrument;
	/**
	 * 检查操作要领
	 */
	private String checkoperationmanual;
	/**
	 * 检查员
	 */
	private String inspector;
	/**
	 * 检查频次
	 */
	private String inspectionfrequency;
	/**
	 * 记录要求
	 */
	private String recordrequirement;
	/**
	 * 巡检人
	 */
	private String patrolinspector;
	/**
	 * 检具校正
	 */
	private String calibrationtool;
	/**
	 * 校正频次
	 */
	private String calibrationfrequency;
	/**
	 * 创建人
	 */
	private String creatPeople;
	/**
	 * 创建时间
	 */
	private Date creatTime;
	/**
	 * 审批flag
	 */
	private String flag;
	/**
	 * 删除flag
	 */
	private String delteflag;

}
