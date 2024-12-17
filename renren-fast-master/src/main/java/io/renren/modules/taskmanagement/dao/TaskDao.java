package io.renren.modules.taskmanagement.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.taskmanagement.entity.TaskLineChartDTO;
import io.renren.modules.taskmanagement.vo.PlanExportVO;
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

    List<TaskEntity> getTasksByUserId(@Param("userId") Long userId);

    List<TaskLineChartDTO> taskLineDisplay(String planId);

    Page<TaskEntity> queryPageGetUnfinishedTasks(Page<TaskEntity> page, TaskEntity task, Long userId);
    Page<TaskEntity> queryPageByParams(Page<TaskEntity> page, TaskEntity task, Long userId);

    List<PlanExportVO> selectTaskByPlanId(String planId);
}
