package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;

import java.util.Map;

/**
 *
 *
 * @author lin
 * @email ${email}
 * @date 2024-08-27 14:22:12
 */
public interface QcExamineStatusService extends IService<QcExamineStatusEntity> {
    boolean saveOrUpdateBySubject(QcExamineStatusEntity qcExamineStatus);
    PageUtils queryPage(Map<String, Object> params);
}

