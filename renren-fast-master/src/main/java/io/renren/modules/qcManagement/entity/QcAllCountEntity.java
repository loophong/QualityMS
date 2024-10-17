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
 * @date 2024-10-13 20:23:51
 */
@Data
@TableName("qc_all_count")
public class QcAllCountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Integer qcAllId;
	/**
	 * 职工总数
	 */
	private Integer qcMemberCount;

}
