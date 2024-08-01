package io.renren.modules.indicator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.indicator.entity.IndicatorTableEntity;

import java.util.Map;

/**
 * 指标表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 14:24:18
 */
public interface IndicatorTableService extends IService<IndicatorTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

