package io.renren.modules.taskmanagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.TaskDataPlanDao;
import io.renren.modules.taskmanagement.entity.TaskDataPlanEntity;
import io.renren.modules.taskmanagement.service.TaskDataPlanService;


@Service("taskDataPlanService")
public class TaskDataPlanServiceImpl extends ServiceImpl<TaskDataPlanDao, TaskDataPlanEntity> implements TaskDataPlanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskDataPlanEntity> page = this.page(
                new Query<TaskDataPlanEntity>().getPage(params),
                new QueryWrapper<TaskDataPlanEntity>()
        );

        return new PageUtils(page);
    }

}