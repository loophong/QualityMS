package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.PlanEntity;

import java.util.List;
import java.util.Map;

/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
public interface PlanService extends IService<PlanEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public boolean isPlanNumberUsed(String planId);

    PlanEntity getPlanByPlanName(String planId);

    List<PlanEntity> getPlansByUserId(String userId);
}

