package io.renren.modules.instrument.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.instrument.entity.InstrumentationEntity;

import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-31 10:36:37
 */
public interface InstrumentationService extends IService<InstrumentationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

