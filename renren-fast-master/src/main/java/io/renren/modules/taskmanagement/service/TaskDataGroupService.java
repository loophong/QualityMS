package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.TaskDataGroupEntity;

import java.util.Map;

/**
 * 
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-18 15:11:00
 */
public interface TaskDataGroupService extends IService<TaskDataGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

