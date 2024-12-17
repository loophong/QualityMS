package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.dao.PlanApprovalTableDao;
import io.renren.modules.taskmanagement.entity.ApprovalStatus;
import io.renren.modules.taskmanagement.entity.PlanApprovalTableEntity;
import io.renren.modules.taskmanagement.service.PlanApprovalTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Slf4j
@Service("PlanApprovalTableService")
public class PlanApprovalTableServiceImpl extends ServiceImpl<PlanApprovalTableDao, PlanApprovalTableEntity> implements PlanApprovalTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        log.info("param"+ params);
        IPage<PlanApprovalTableEntity> page = this.page(
                new Query<PlanApprovalTableEntity>().getPage(params),
                new LambdaQueryWrapper<PlanApprovalTableEntity>()
                        .eq(PlanApprovalTableEntity::getApprovalStatus, ApprovalStatus.PENDING)
                        .eq(PlanApprovalTableEntity::getApprover, ShiroUtils.getUserId())
                        .orderByDesc(PlanApprovalTableEntity::getPlanSubmissionTime)
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageGetMySubmitApprovalList(Map<String, Object> params) {
        log.info("param"+ params);
        // 分页查询我提交的审批，按时间倒序，并且把未审批的放在第一页
        IPage<PlanApprovalTableEntity> page = this.page(
                new Query<PlanApprovalTableEntity>().getPage(params),
                new LambdaQueryWrapper<PlanApprovalTableEntity>()
                        .eq(PlanApprovalTableEntity::getSubmitter, ShiroUtils.getUserId())
                        .orderByAsc(PlanApprovalTableEntity::getApprovalStatus) // 未审批的审批状态值较小
                        .orderByDesc(PlanApprovalTableEntity::getPlanSubmissionTime)
        );
        return new PageUtils(page);
    }

}