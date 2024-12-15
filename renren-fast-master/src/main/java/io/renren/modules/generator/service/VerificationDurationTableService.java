package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.VerificationDurationTableEntity;

import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-10 16:51:55
 */
public interface VerificationDurationTableService extends IService<VerificationDurationTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    VerificationDurationTableEntity getLatestRecord();
}

