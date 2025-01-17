package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.ApprovalDao;
import io.renren.modules.taskmanagement.service.ApprovalService;

@Service("taskManagementApprovalTableService")
@Slf4j
public class ApprovalServiceImpl extends ServiceImpl<ApprovalDao, ApprovalEntity> implements ApprovalService {

    @Autowired
    private TaskService taskService;

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

    @Override
    public void createTaskApproval(TaskEntity task, String approvalType) {

        ApprovalEntity approvalEntity = new ApprovalEntity();
        approvalEntity.setApprovalContent(null);
        approvalEntity.setTaskId(task.getTaskId());
        approvalEntity.setTaskName(task.getTaskName());
        approvalEntity.setTaskContent(task.getTaskContent());
        approvalEntity.setTaskAssociatedPlanId(task.getTaskAssociatedPlanId());
        approvalEntity.setTaskPrincipal(task.getTaskPrincipal());

        log.info("当前任务的AssociatedIndicatorsId为：" + task.getTaskAssociatedIndicatorsId());
        if (task.getTaskAssociatedIndicatorsId() != null && !task.getTaskAssociatedIndicatorsId().equals("")) {
            approvalEntity.setTaskAssociatedIndicatorsId(task.getTaskAssociatedIndicatorsId());
        }
        //获取当前时间
        approvalEntity.setTaskStartDate(task.getTaskStartDate());
        approvalEntity.setTaskSubmissionTime(new Date());
        approvalEntity.setApprover(task.getTaskAuditor());
        approvalEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));

        approvalEntity.setApprovalType(approvalType);

        this.save(approvalEntity);


    }

}