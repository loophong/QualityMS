package io.renren.modules.qcManagement.entity;

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
 * @date 2024-10-20 17:53:45
 */
@Data
@TableName("qc_tools_template")
public class QcToolsTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 模版主键id
	 */
	@TableId
	private Integer templateId;
	/**
	 * 模版名
	 */
	private String templateName;
	/**
	 * 模版类型
	 */
	private String templateType;
	/**
	 * 系列数据
	 */
	private String templateSeries;
	/**
	 * x轴数据
	 */
	private String templateAxis;
	/**
	 * 标题名
	 */
	private String templateText;


}
