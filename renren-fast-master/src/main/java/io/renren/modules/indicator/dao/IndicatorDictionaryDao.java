package io.renren.modules.indicator.dao;

import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.indicator.entity.IndicatorResponseByClassificationEntity;
import io.renren.modules.indicator.entity.IndicatorResponseByDepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 17:55:09
 */
@Mapper
public interface IndicatorDictionaryDao extends BaseMapper<IndicatorDictionaryEntity> {

    public List<IndicatorResponseByDepartmentEntity> countsByDepartmant(IndicatorResponseByDepartmentEntity indicatorResponseByDepartmentEntity);

    public List<IndicatorResponseByClassificationEntity> countsByClassification(IndicatorResponseByClassificationEntity indicatorResponseByClassification);
	
}
