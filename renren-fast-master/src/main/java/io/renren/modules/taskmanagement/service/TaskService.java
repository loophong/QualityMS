package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.dto.TaskQueryParamDTO;
import io.renren.modules.taskmanagement.entity.TaskDetailDTO;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.entity.TaskLineChartDTO;
import io.renren.modules.taskmanagement.entity.TaskStatisticsDTO;

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

    PageUtils queryPageGetUnfinishedTasks(TaskQueryParamDTO taskQueryParamDTO, Long userId);


    PageUtils queryPageGetCompletedTasksList(Map<String, Object> params, Long userId);

    PageUtils queryPageGetCompletedTasksList(TaskQueryParamDTO taskQueryParamDTO, Long userId);


    TaskDetailDTO getTaskDetailInfo(String taskId);

    int saveDecompositionTasks(List<TaskEntity> tasks);

    boolean isTaskIdUsed(String taskId);

    TaskStatisticsDTO taskStatistics(String planId);

    PageUtils queryPageSelectTasksByPlanId(Map<String, Object> params, String planId);

    List<Map<String, Integer>> home();

    List<TaskLineChartDTO> taskLineDisplay(String planId);



//    void selectAllTasksByPlanId(String planId);
}

