package io.renren.modules.instrument.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.instrument.entity.InstrumentTestMethodEntity;

import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-31 10:55:46
 */
public interface InstrumentTestMethodService extends IService<InstrumentTestMethodEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

