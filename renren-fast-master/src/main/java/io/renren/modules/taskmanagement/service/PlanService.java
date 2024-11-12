package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.dto.PlanDTO;
import io.renren.modules.taskmanagement.dto.PlanQueryParamDTO;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.entity.PlanStatisticsLabelDto;

import java.util.List;
import java.util.Map;

/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
public interface PlanService extends IService<PlanEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public boolean isPlanNumberUsed(String planId);

    PlanEntity getPlanByPlanName(String planId);

    List<PlanEntity> getPlansByUserId(String userId);

    List<Map<String, Integer>> home();

    List<PlanStatisticsLabelDto> getPlanLabel();

    PageUtils queryPageUnfinishedPlan(Map<String, Object> params);

    PageUtils queryPageFinishedPlan(Map<String, Object> params);

    PageUtils queryPageGetHistoryPlan(Map<String, Object> params);

    void saveAllPlanInfo(PlanDTO planDTO);

    void updateAllPlanInfo(PlanDTO planDTO);

    PageUtils queryPageByParams(PlanQueryParamDTO planQueryParamDTO);

    PageUtils queryPageFinishedPlan(PlanQueryParamDTO planQueryParamDTO);
}

