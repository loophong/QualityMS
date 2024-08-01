package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车型字典
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 18:55:06
 */
@Mapper
public interface IssueCarTypeTableDao extends BaseMapper<IssueCarTypeTableEntity> {
	
}
