package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.IssueTypeTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 问题类别字典
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 10:59:16
 */
@Mapper
public interface IssueTypeTableDao extends BaseMapper<IssueTypeTableEntity> {
	
}
