package io.renren.modules.indicator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;

import java.util.Map;

/**
 * 
 *
 * @author lin
 * @email ${email}
 * @date 2024-08-14 16:51:50
 */
public interface IndicatorDataDictionaryService extends IService<IndicatorDataDictionaryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

