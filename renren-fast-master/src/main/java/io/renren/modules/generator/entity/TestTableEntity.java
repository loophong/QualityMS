package io.renren.modules.generator.entity;

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
 * @date 2024-07-18 08:58:50
 */
@Data
@TableName("Test_Table")
public class TestTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String hongGroupNumber;
	/**
	 * 
	 */
	private Date startTime;
	/**
	 * 
	 */
	private Date endTime;

}
