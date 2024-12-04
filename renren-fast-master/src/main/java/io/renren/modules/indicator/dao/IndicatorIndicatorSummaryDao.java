package io.renren.modules.indicator.dao;

import io.renren.modules.indicator.entity.IndicatorChart1Entity;
import io.renren.modules.indicator.entity.IndicatorChart2Entity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-20 14:17:04
 */
@Mapper
public interface IndicatorIndicatorSummaryDao extends BaseMapper<IndicatorIndicatorSummaryEntity> {

   public List<IndicatorChart1Entity> queryChart1List(IndicatorChart1Entity indicatorChart1Entity);

   public List<IndicatorChart2Entity> queryChart2List(IndicatorChart2Entity indicatorChart2Entity);
}
