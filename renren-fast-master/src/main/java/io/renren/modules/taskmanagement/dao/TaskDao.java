package io.renren.modules.taskmanagement.dao;

import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务管理模块任务表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {

    List<TaskEntity> getTasksByUserId(@Param("userId") String userId);
	
}
