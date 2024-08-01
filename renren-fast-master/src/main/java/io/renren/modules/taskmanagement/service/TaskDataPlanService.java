package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.TaskDataPlanEntity;

import java.util.Map;

/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-18 16:27:33
 */
public interface TaskDataPlanService extends IService<TaskDataPlanEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

