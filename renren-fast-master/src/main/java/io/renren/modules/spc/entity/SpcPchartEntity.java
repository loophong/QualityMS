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


	// 对 sampling_SpcPchartEntity 的示例方法
	public void setDate(int index, int value) {
		switch (index) {
			case 1: setDate1(value); break;
			case 2: setDate2(value); break;
			case 3: setDate3(value); break;
			case 4: setDate4(value); break;
			case 5: setDate5(value); break;
			case 6: setDate6(value); break;
			case 7: setDate7(value); break;
			case 8: setDate8(value); break;
			case 9: setDate9(value); break;
			case 10: setDate10(value); break;
			case 11: setDate11(value); break;
			case 12: setDate12(value); break;
			case 13: setDate13(value); break;
			case 14: setDate14(value); break;
			case 15: setDate15(value); break;
			case 16: setDate16(value); break;
			case 17: setDate17(value); break;
			case 18: setDate18(value); break;
			case 19: setDate19(value); break;
			case 20: setDate20(value); break;
			case 21: setDate21(value); break;
			case 22: setDate22(value); break;
			case 23: setDate23(value); break;
			case 24: setDate24(value); break;
			case 25: setDate25(value); break;
			case 26: setDate26(value); break;
			case 27: setDate27(value); break;
			case 28: setDate28(value); break;
			case 29: setDate29(value); break;
			case 30: setDate30(value); break;
			case 31: setDate31(value); break;
		}
	}

	// 对 sampling_SpcPchartEntity 的示例方法
	public Integer getDate(int index) {
		switch (index) {
			case 1: return getDate1();
			case 2: return getDate2();
			case 3: return getDate3();
			case 4: return getDate4();
			case 5: return getDate5();
			case 6: return getDate6();
			case 7: return getDate7();
			case 8: return getDate8();
			case 9: return getDate9();
			case 10: return getDate10();
			case 11: return getDate11();
			case 12: return getDate12();
			case 13: return getDate13();
			case 14: return getDate14();
			case 15: return getDate15();
			case 16: return getDate16();
			case 17: return getDate17();
			case 18: return getDate18();
			case 19: return getDate19();
			case 20: return getDate20();
			case 21: return getDate21();
			case 22: return getDate22();
			case 23: return getDate23();
			case 24: return getDate24();
			case 25: return getDate25();
			case 26: return getDate26();
			case 27: return getDate27();
			case 28: return getDate28();
			case 29: return getDate29();
			case 30: return getDate30();
			case 31: return getDate31();
			default: throw new IllegalArgumentException("Index must be between 1 and 31"); // 可以抛出异常用于无效索引
		}
	}
}
