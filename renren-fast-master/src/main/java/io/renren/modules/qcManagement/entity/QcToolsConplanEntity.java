package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-20 17:53:45
 */
@Data
@TableName("qc_tools_conplan")
public class QcToolsConplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 模版主键id
	 */
	@TableId
	private Integer conplanId;
	/**
	 * 模版名
	 */
	private String conplanName;
	/**
	 * 模版类型
	 */
	private String conplanType;
	/**
	 * 系列数据
	 */
	private String conplanSeries;
	/**
	 * x轴数据
	 */
	private String conplanAxis;
	/**
	 * 标题名
	 */
	private String conplanText;

    /**
     * 所属subject
     */
    private Integer conplanSubject;
    /**
     * 所属process
     */
    private  Integer conplanProcess;
	/**
	 * 图片url
	 */
	private  String conplanUrl;


}
