package io.renren.modules.taskmanagement.service.impl;

import io.renren.modules.taskmanagement.entity.ApprovalStatus;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.ApprovalDao;
import io.renren.modules.taskmanagement.entity.ApprovalEntity;
import io.renren.modules.taskmanagement.service.ApprovalService;


@Service("taskManagementApprovalTableService")
public class ApprovalServiceImpl extends ServiceImpl<ApprovalDao, ApprovalEntity> implements ApprovalService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ApprovalEntity> page = this.page(
                new Query<ApprovalEntity>().getPage(params),
                new QueryWrapper<ApprovalEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageMyApprovalList(Map<String, Object> params, Long userId) {
        //审批人为当前用户
        IPage<ApprovalEntity> page = this.page(
                new Query<ApprovalEntity>().getPage(params),
                new QueryWrapper<ApprovalEntity>().eq("approver", userId)
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPagePendingApprovalsList(Map<String, Object> params, Long userId) {
        //审批人为当前用户,审批状态为待审批
        IPage<ApprovalEntity> page = this.page(
                new Query<ApprovalEntity>().getPage(params),
                new QueryWrapper<ApprovalEntity>().eq("approver", userId).and(wrapper -> wrapper.eq("approval_status", ApprovalStatus.PENDING))
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageHistoryApprovalList(Map<String, Object> params, Long userId) {
        //审批人为当前用户,审批状态不为待审批
        IPage<ApprovalEntity> page = this.page(
                new Query<ApprovalEntity>().getPage(params),
                new QueryWrapper<ApprovalEntity>().eq("approver", userId).and(wrapper -> wrapper.ne("approval_status", ApprovalStatus.PENDING))
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageGetMySubmitApprovalList(Map<String, Object> params, Long userId) {
        IPage<ApprovalEntity> page = this.page(
                new Query<ApprovalEntity>().getPage(params),
                new QueryWrapper<ApprovalEntity>().eq("submitter", userId)
                        .orderByDesc("task_submission_time")
        );

        return new PageUtils(page);
    }

}