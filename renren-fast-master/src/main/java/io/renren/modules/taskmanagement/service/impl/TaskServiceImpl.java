package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Override
    public List<TaskEntity> getTasksByPlanId(String planId) {
        return taskDao.selectList(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId));
    }

    @Override
    public List<TaskEntity> getTasksByUserId(String userId) {
        return taskDao.getTasksByUserId(userId);
    }
}