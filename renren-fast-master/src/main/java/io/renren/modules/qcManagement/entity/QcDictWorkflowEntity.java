package io.renren.modules.qcManagement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 09:22:11
 */
@Data
@TableName("qc_dict_workFlow")
public class QcDictWorkflowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id(主键)
	 */
	@TableId
	private Long dictId;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 阶段
	 */
	private String phase;
	/**
	 * 步骤
	 */
	private String step;
	/**
	 * 补充描述
	 */
	private String description;

}
