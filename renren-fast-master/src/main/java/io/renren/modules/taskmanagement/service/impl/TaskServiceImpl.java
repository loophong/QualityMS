package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.TaskDao;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.service.TaskService;


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
    public TaskEntity getByTaskId(Long taskId) {
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
                        .and(w -> w.notIn("task_current_state", TaskStatus.COMPLETED))
                        .and(w -> w.eq("task_principal", userId))
                        .or(w -> w.like("task_executor", executorIdLike))
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