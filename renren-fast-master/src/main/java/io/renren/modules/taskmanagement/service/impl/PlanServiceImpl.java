package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}