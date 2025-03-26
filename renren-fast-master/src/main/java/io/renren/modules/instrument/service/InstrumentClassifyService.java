package io.renren.modules.instrument.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.instrument.entity.InstrumentClassifyEntity;

import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-16 16:51:59
 */
public interface InstrumentClassifyService extends IService<InstrumentClassifyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

