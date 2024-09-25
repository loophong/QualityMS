package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.taskmanagement.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.TaskDao;
import io.renren.modules.taskmanagement.service.TaskService;


@Slf4j
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>()

        );

        return new PageUtils(page);
    }

    //使用Ipage分页，查询task中未开始的任务
    @Override
    public PageUtils queryPageSelectTasksByUserId(Map<String, Object> params, Long userId) {

        // 模糊查询执行人ID,"userid"拼接双引号
        String executorIdLike = "\"" + userId + "\"";

        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>()
                        .or(w -> w.eq("task_principal", userId))
                        .or(w -> w.eq("task_auditor", userId))
                        .or(w -> w.like("task_executor", executorIdLike))
        );

        return new PageUtils(page);
    }

    @Override
    public TaskEntity getByTaskId(String taskId) {
        //使用mybatis-plus通过taskId查询task
        LambdaQueryWrapper<TaskEntity> eq = new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskId, taskId);
        return taskDao.selectOne(eq);

    }

    /**
     * @description: 查询当前用户为未完成任务，负责人 和 执行人 都算
     * @return: io.renren.common.utils.PageUtils
     * @author: hong
     * @date: 2024/8/23 16:00
     */
    @Override
    public PageUtils queryPageGetUnfinishedTasks(Map<String, Object> params, Long userId) {
        // 模糊查询执行人ID,"userid"拼接双引号
        String executorIdLike = "\"" + userId + "\"";

        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>()
                        // 第一个条件必须成立
                        .notIn("task_current_state", TaskStatus.COMPLETED)
                        // 后两个条件为“或”的关系，并且这组条件必须成立
                        .and(w ->
                                w.eq("task_principal", userId)
                                        .or().like("task_executor", executorIdLike)
                        )
//                new QueryWrapper<TaskEntity>()
//                        .and(w -> w.notIn("task_current_state", TaskStatus.COMPLETED))
//                        .and(w -> w.eq("task_principal", userId))
//                        .or(w -> w.like("task_executor", executorIdLike))
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageGetCompletedTasksList(Map<String, Object> params, Long userId) {
        // 模糊查询执行人ID,"userid"拼接双引号
        String executorIdLike = "\"" + userId + "\"";

        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>().eq("task_current_state", TaskStatus.COMPLETED)
                        .and(w -> {
                            w.eq("task_principal", userId)
                                    .or(i -> i.like("task_executor", executorIdLike));
                        })
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageSelectTasksByPlanId(Map<String, Object> params, String planId) {
        IPage<TaskEntity> page = this.page(
                new Query<TaskEntity>().getPage(params),
                new QueryWrapper<TaskEntity>().eq("task_associated_plan_id", planId)
        );
        return new PageUtils(page);
    }

    @Override
    public List<Map<String, Integer>> home() {
        int year = DateUtils.getZeroTime().getYear()+1900;
        log.info("year" + year);

        // 查询当年的任务数量,计划开始时间年份模糊匹配 year
        int taskNum = taskDao.selectList(new QueryWrapper<TaskEntity>().like("task_start_date", year)).size();
        // 查询当年 已完成 任务数量,计划开始时间年份模糊匹配 year
        int completedTaskNum = taskDao.selectList(new QueryWrapper<TaskEntity>().eq("task_current_state", TaskStatus.COMPLETED).like("task_start_date", year)).size();

        List<Map<String, Integer>> maps = new ArrayList<>();
        Map<String, Integer> taskNumMap = new HashMap<>();
        taskNumMap.put("taskNum", taskNum);
        maps.add(taskNumMap);

        Map<String, Integer> completedTaskNumMap = new HashMap<>();
        completedTaskNumMap.put("completedTaskNum", completedTaskNum);
        maps.add(completedTaskNumMap);

        return maps;
    }

    @Override
    public TaskDetailDTO getTaskDetailInfo(String taskId) {
        TaskEntity task = taskDao.selectOne(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskId, taskId));

        // taskParentNode = taskId 查询列表
        List<TaskEntity> tasks = taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskParentNode, taskId));

        log.info("rootNode" + task);
        log.info("tasks" + tasks);

        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();

        // 封装任务信息
        taskDetailDTO.setRootTask(task);
        // 封装任务子任务信息
        taskDetailDTO.setTasks(tasks);

        return taskDetailDTO;


    }

    @Override
    public int saveDecompositionTasks(List<TaskEntity> tasks) {
        if (tasks != null && tasks.size() > 0) {

            for (TaskEntity task : tasks) {
                TaskEntity task1 = taskDao.selectById(task.getTmTid());
                log.info("当前task为" + task.getTaskId());
                log.info("当前task是否存在" + task1);
                if (task1 != null) {
                    // 更新
                    taskDao.updateById(task);
                } else {
                    // 插入
                    taskDao.insert(task);
                }
            }

            // 根据taskId检查当前数据是否存在，如果存在则更新，不存在则插入


        }
        return 0;
    }

    @Override
    public boolean isTaskIdUsed(String taskId) {
//        return planDao.countByPlanNumber(planId) > 0;
        return taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskId, taskId)).size() > 0;
    }

    @Override
    public TaskStatisticsDTO taskStatistics(String planId) {
        TaskStatisticsDTO taskStatistics = new TaskStatisticsDTO();
        // 任务列表
        List<TaskEntity> tasks = taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId));
        // 已完成任务
        List<TaskEntity> completedTasks = taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId).eq(TaskEntity::getTaskCurrentState, TaskStatus.COMPLETED));

        log.info("planId" + planId);
        log.info("tasks" + tasks);
        log.info("completedTasks" + completedTasks);

        int taskAheadOfTimeNum = 0;
        int taskOnTimeNum = 0;
        int taskBehindScheduleNum = 0;

        // 遍历tasks
        for (TaskEntity task : completedTasks) {
            if (task.getTaskCurrentState() == TaskStatus.COMPLETED) {
                // 计算时间差
                long dif = task.getTaskActualCompletionDate().getTime() - task.getTaskScheduleCompletionDate().getTime();

                if (dif > 0) {
                    taskBehindScheduleNum++;
                } else if (dif == 0) {
                    taskOnTimeNum++;
                } else {
                    taskAheadOfTimeNum++;
                }
            }
        }

        // 任务总数
        taskStatistics.setTaskTotal(tasks.size());
        // 已完成任务数
        taskStatistics.setTaskCompleted(completedTasks.size());
        // 未完成任务数
        taskStatistics.setTaskUncompleted(tasks.size() - completedTasks.size());
        // 提前完成
        taskStatistics.setTaskAheadOfTime(taskAheadOfTimeNum);
        // 提前完成率
        taskStatistics.setTaskAheadOfTimeRate(taskAheadOfTimeNum * 100.0 / tasks.size());
        // 滞后完成
        taskStatistics.setTaskBehindSchedule(taskBehindScheduleNum);
        // 按时完成
        taskStatistics.setTaskOnTime(taskOnTimeNum);
        // 按时完成率
        taskStatistics.setTaskOnTimeRate(taskOnTimeNum * 100.0 / tasks.size());
        // 任务列表
        taskStatistics.setTaskList(tasks);


        return taskStatistics;
    }


    @Override
    public List<TaskEntity> getTasksByPlanId(String planId) {
        return taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId));
    }

    @Override
    public List<TaskEntity> getTasksByUserId(Long userId) {
        return taskDao.getTasksByUserId(userId);
    }

    @Override
    public List<TaskEntity> selectTasksByUserId(Long userId) {
        return taskDao.getTasksByUserId(userId);
    }


}