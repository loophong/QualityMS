package io.renren.modules.indicator.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 18:47:20
 */
@Mapper
public interface IndicatorKeyIndicatorsDao extends BaseMapper<IndicatorKeyIndicatorsEntity> {


    Page<IndicatorKeyIndicatorsEntity> selectFinishedSubjectList(Page<IndicatorKeyIndicatorsEntity> page);
}
