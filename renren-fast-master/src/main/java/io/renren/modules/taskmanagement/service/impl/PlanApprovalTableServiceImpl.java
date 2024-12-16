package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.dao.PlanApprovalTableDao;
import io.renren.modules.taskmanagement.entity.ApprovalStatus;
import io.renren.modules.taskmanagement.entity.PlanApprovalTableEntity;
import io.renren.modules.taskmanagement.service.PlanApprovalTableService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;




@Service("PlanApprovalTableService")
public class PlanApprovalTableServiceImpl extends ServiceImpl<PlanApprovalTableDao, PlanApprovalTableEntity> implements PlanApprovalTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PlanApprovalTableEntity> page = this.page(
                new Query<PlanApprovalTableEntity>().getPage(params),
                new LambdaQueryWrapper<PlanApprovalTableEntity>()
                        .eq(PlanApprovalTableEntity::getApprovalStatus, ApprovalStatus.PENDING)
                        .eq(PlanApprovalTableEntity::getApprover, ShiroUtils.getUserId())
                        .orderByDesc(PlanApprovalTableEntity::getPlanSubmissionTime)
        );

        return new PageUtils(page);
    }

}