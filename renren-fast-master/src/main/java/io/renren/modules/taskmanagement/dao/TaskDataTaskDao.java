package io.renren.modules.taskmanagement.dao;

import io.renren.modules.taskmanagement.entity.TaskDataTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务管理模块任务表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-18 16:27:33
 */
@Mapper
public interface TaskDataTaskDao extends BaseMapper<TaskDataTaskEntity> {
	
}
