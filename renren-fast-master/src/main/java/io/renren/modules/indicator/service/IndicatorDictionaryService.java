package io.renren.modules.indicator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.entity.IndicatorResponseByClassificationEntity;
import io.renren.modules.indicator.entity.IndicatorResponseByDepartmentEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 17:55:09
 */
public interface IndicatorDictionaryService extends IService<IndicatorDictionaryEntity> {

    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryIndicatorList(Map<String, Object> params);
    public List<IndicatorDictionaryEntity> getDictionaryEntitiesByName(String indicatorName);

    public List<IndicatorResponseByDepartmentEntity> countsByDepartmant(IndicatorResponseByDepartmentEntity indicatorResponseByDepartmentEntity);

    public List<IndicatorResponseByClassificationEntity> countsByClassification(IndicatorResponseByClassificationEntity indicatorResponseByClassificationEntity);

}

