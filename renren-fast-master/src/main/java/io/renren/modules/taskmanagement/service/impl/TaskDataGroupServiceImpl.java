package io.renren.modules.taskmanagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.TaskDataGroupDao;
import io.renren.modules.taskmanagement.entity.TaskDataGroupEntity;
import io.renren.modules.taskmanagement.service.TaskDataGroupService;


@Service("taskDataGroupService")
public class TaskDataGroupServiceImpl extends ServiceImpl<TaskDataGroupDao, TaskDataGroupEntity> implements TaskDataGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskDataGroupEntity> page = this.page(
                new Query<TaskDataGroupEntity>().getPage(params),
                new QueryWrapper<TaskDataGroupEntity>()
        );

        return new PageUtils(page);
    }

}