package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 问题类别字典
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 10:59:16
 */
@Data
@TableName("issue_type_table")
public class IssueTypeTableEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 问题类别ID
	 */
	@TableId
	private Integer issueCategoryId;
	/**
	 * 具体问题类别
	 */
	private String concreteIssueCategory;

}
