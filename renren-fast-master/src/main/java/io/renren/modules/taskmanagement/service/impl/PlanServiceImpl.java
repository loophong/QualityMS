package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.taskmanagement.entity.PlanStatisticsLabelDto;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.PlanDao;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.service.PlanService;


@Service("planService")
public class PlanServiceImpl extends ServiceImpl<PlanDao, PlanEntity> implements PlanService {

    @Autowired
    private PlanDao planDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PlanEntity> page = this.page(
                new Query<PlanEntity>().getPage(params),
                new QueryWrapper<PlanEntity>()
        );

        System.out.println(page.getRecords().toString());

        return new PageUtils(page);
    }

    @Override
    public boolean isPlanNumberUsed(String planId) {
        return planDao.countByPlanNumber(planId) > 0;
    }

    @Override
    public PlanEntity getPlanByPlanName(String planId) {
        return planDao.selectOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));

    }

    @Override
    public List<PlanEntity> getPlansByUserId(String userId) {
        return planDao.getPlansByUserId(userId);
    }

    @Override
    public List<Map<String, Integer>> home() {
        int year = DateUtils.getZeroTime().getYear()+1900;

        // 查询当年的计划数量,计划开始时间年份模糊匹配 year
        int planNum = planDao.selectList(new QueryWrapper<PlanEntity>().like("plan_start_date", year)).size();
        // 查询当年 已完成 计划数量,计划开始时间年份模糊匹配 year
        int completedPlanNum = planDao.selectList(new QueryWrapper<PlanEntity>().eq("plan_current_state", TaskStatus.COMPLETED).like("plan_start_date", year)).size();

        List<Map<String, Integer>> maps = new ArrayList<>();
        Map<String, Integer> planNumMap = new HashMap<>();
        planNumMap.put("planNum", planNum);
        maps.add(planNumMap);

        Map<String, Integer> completedPlanNumMap = new HashMap<>();
        completedPlanNumMap.put("completedPlanNum", completedPlanNum);
        maps.add(completedPlanNumMap);

        return maps;
    }

    @Override
    public List<PlanStatisticsLabelDto> getPlanLabel() {
        return planDao.getPlanLabel();
    }

}