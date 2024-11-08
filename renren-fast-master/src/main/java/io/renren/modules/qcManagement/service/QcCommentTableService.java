package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcCommentTableEntity;

import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-28 16:16:44
 */
public interface QcCommentTableService extends IService<QcCommentTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

