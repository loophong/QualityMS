package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;

import io.renren.modules.taskmanagement.entity.PlanApprovalTableEntity;

import java.util.Map;

/**
 * 任务审批表
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-15 14:35:09
 */
public interface PlanApprovalTableService extends IService<PlanApprovalTableEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

