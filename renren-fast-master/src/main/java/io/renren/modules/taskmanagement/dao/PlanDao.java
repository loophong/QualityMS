package io.renren.modules.taskmanagement.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.taskmanagement.entity.PlanStatisticsLabelDto;
import io.renren.modules.taskmanagement.vo.PlanExportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 任务管理模块计划表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Mapper
public interface PlanDao extends BaseMapper<PlanEntity> {

    @Select("SELECT COUNT(*) FROM task_management_plan WHERE plan_id = #{planId}")
    int countByPlanNumber(@Param("planId") String planId);

    List<PlanEntity> getPlansByUserId(@Param("userId") String userId);

    List<PlanStatisticsLabelDto> getPlanLabel();

    Page<PlanEntity> queryPageByParams(Page<PlanEntity> page, PlanEntity plan);

    List<PlanExportVO> export();
    List<PlanExportVO> exportKnowledgeBase();

    List<PlanExportVO> exportBase();
}
