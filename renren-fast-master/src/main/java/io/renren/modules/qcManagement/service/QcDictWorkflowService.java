package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcDictWorkflowEntity;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 09:22:11
 */
public interface QcDictWorkflowService extends IService<QcDictWorkflowEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

