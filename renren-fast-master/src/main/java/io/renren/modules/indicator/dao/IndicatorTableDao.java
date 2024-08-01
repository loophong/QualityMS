package io.renren.modules.indicator.dao;

import io.renren.modules.indicator.entity.IndicatorTableEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 指标表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 14:24:18
 */
@Mapper
public interface IndicatorTableDao extends BaseMapper<IndicatorTableEntity> {
	
}
