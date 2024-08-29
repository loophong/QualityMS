package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcStepEntity;

import java.util.Map;

/**
 *
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-25 14:45:45
 */
public interface QcStepService extends IService<QcStepEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


