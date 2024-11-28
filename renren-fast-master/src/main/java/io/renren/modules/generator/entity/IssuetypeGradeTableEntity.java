package io.renren.modules.generator.entity;

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
 * @date 2024-11-25 14:50:00
 */
@Data
@TableName("issuetype_grade_table")
public class IssuetypeGradeTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer gradeId;
	/**
	 * 等级
	 */
	private String grade;
	/**
	 * 等级说明
	 */
	private String gradeIllustrate;

}
