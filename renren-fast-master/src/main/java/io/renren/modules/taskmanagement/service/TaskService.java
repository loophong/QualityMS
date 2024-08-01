package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.TaskEntity;

import java.util.List;
import java.util.Map;

/**
 * 任务管理模块任务表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<TaskEntity> getTasksByPlanId(String planId);

    List<TaskEntity> getTasksByUserId(String userId);
}

