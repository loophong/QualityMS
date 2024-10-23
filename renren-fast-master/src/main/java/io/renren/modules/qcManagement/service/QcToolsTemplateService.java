package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;

import java.util.Map;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-20 17:53:45
 */
public interface QcToolsTemplateService extends IService<QcToolsTemplateEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

