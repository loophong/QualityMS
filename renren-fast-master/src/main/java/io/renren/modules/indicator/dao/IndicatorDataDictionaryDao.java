package io.renren.modules.indicator.dao;

import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.indicator.entity.IndicatorListEntityDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author lin
 * @email ${email}
 * @date 2024-08-14 16:51:50
 */
@Mapper
public interface IndicatorDataDictionaryDao extends BaseMapper<IndicatorDataDictionaryEntity> {

    List<IndicatorListEntityDTO> getIndicatorsList();
}
