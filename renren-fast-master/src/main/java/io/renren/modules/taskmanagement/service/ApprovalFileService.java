package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.ApprovalFileEntity;

import java.util.Map;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-10 15:50:58
 */
public interface ApprovalFileService extends IService<ApprovalFileEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
