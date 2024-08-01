package io.renren.modules.taskmanagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.TaskDataTaskDao;
import io.renren.modules.taskmanagement.entity.TaskDataTaskEntity;
import io.renren.modules.taskmanagement.service.TaskDataTaskService;


@Service("taskDataTaskService")
public class TaskDataTaskServiceImpl extends ServiceImpl<TaskDataTaskDao, TaskDataTaskEntity> implements TaskDataTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskDataTaskEntity> page = this.page(
                new Query<TaskDataTaskEntity>().getPage(params),
                new QueryWrapper<TaskDataTaskEntity>()
        );

        return new PageUtils(page);
    }

}