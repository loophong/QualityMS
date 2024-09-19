package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.TaskDetailDTO;
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

    List<TaskEntity> getTasksByUserId(Long userId);

    List<TaskEntity> selectTasksByUserId(Long userId);

     PageUtils queryPageSelectTasksByUserId(Map<String, Object> params, Long userId);

     TaskEntity getByTaskId(String taskId);

    PageUtils queryPageGetUnfinishedTasks(Map<String, Object> params, Long userId);

    PageUtils queryPageGetCompletedTasksList(Map<String, Object> params, Long userId);

    TaskDetailDTO getTaskDetailInfo(String taskId);

    int saveDecompositionTasks(List<TaskEntity> tasks);

    boolean isTaskIdUsed(String taskId);


//    void selectAllTasksByPlanId(String planId);
}

