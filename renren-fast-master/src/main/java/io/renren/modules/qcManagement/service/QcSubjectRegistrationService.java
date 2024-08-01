package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
public interface QcSubjectRegistrationService extends IService<QcSubjectRegistrationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

