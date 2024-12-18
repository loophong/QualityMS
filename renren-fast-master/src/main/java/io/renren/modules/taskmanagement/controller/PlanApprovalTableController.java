package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.PlanApprovalTableService;
import io.renren.modules.taskmanagement.service.PlanService;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 任务审批表
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-15 14:35:09
 */
@Slf4j
@RestController
@RequestMapping("taskmanagement/planApproval")
public class PlanApprovalTableController {
    @Autowired
    private PlanApprovalTableService planApprovalTableService;
    @Autowired
    private PlanService planService;
    @Autowired
    private TaskService taskService;

    /** 
     * @description: 进行审批
     * @param: planApprovalTable 
     * @return: io.renren.common.utils.R 
     * @author: hong
     * @date: 2024/12/16 15:43
     */
    @Transactional
    @PostMapping("/approval")
    public R approval(@RequestBody PlanApprovalTableEntity planApprovalTable){
        log.info("提交审批" + planApprovalTable);
        //更新审批信息
        PlanApprovalTableEntity approvalTable = planApprovalTableService.lambdaQuery()
                .eq(PlanApprovalTableEntity::getPlanId, planApprovalTable.getPlanId())
                .eq(PlanApprovalTableEntity::getApprovalStatus, ApprovalStatus.PENDING)
                .orderByDesc(PlanApprovalTableEntity::getPlanSubmissionTime).one();
        approvalTable.setApprovalStatus(planApprovalTable.getApprovalStatus());
        approvalTable.setRemarks(planApprovalTable.getRemarks());
        approvalTable.setApprovalComments(planApprovalTable.getApprovalComments());
        planApprovalTableService.updateById(approvalTable);

        // 更新计划状态
        PlanEntity plan = planService.getOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planApprovalTable.getPlanId()));
        if (planApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED){
            plan.setPlanCurrentState(TaskStatus.NOT_STARTED);
        }else if (planApprovalTable.getApprovalStatus() == ApprovalStatus.REJECTED){
            plan.setPlanCurrentState(TaskStatus.PREAPPROVAL_NOT_PASSED);
        }
        planService.updateById(plan);

        // 更新任务状态
        List<TaskEntity> taskList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planApprovalTable.getPlanId()));
        log.info("更新任务状态"+taskList);
        for (TaskEntity task : taskList) {
            if (planApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED){
                task.setTaskCurrentState(TaskStatus.NOT_STARTED);
            }else if (planApprovalTable.getApprovalStatus() == ApprovalStatus.REJECTED){
                task.setTaskCurrentState(TaskStatus.PREAPPROVAL_NOT_PASSED);
            }
            taskService.updateById(task);
        }
        return R.ok();
    }

    /** 
     * @description: 计划取消初审
     * @param: planId 
     * @return: io.renren.common.utils.R 
     * @author: hong
     * @date: 2024/12/16 15:04
     */
    @GetMapping("/cancelPreApproval/{planId}")
    public R approval(@PathVariable("planId") String planId){
        PlanEntity plan = planService.getOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));
        plan.setPlanCurrentState(TaskStatus.CREATED_BUT_NOT_APPROVED);
        planService.updateById(plan);
        List<PlanApprovalTableEntity> list = planApprovalTableService.list(new LambdaQueryWrapper<PlanApprovalTableEntity>().eq(PlanApprovalTableEntity::getPlanId, planId));
        for (PlanApprovalTableEntity planApprovalTable : list) {
            if(planApprovalTable.getApprovalStatus() == ApprovalStatus.PENDING){
                planApprovalTable.setApprovalStatus(ApprovalStatus.CANCEL);
            }
            planApprovalTableService.updateById(planApprovalTable);
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/unApprovalList")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = planApprovalTableService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/getMySubmitApprovalList")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:list")
    public R getMySubmitApprovalList(@RequestParam Map<String, Object> params){
        PageUtils page = planApprovalTableService.queryPageGetMySubmitApprovalList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{approvalId}")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:info")
    public R info(@PathVariable("approvalId") Long approvalId){
		PlanApprovalTableEntity planApprovalTable = planApprovalTableService.getById(approvalId);

        return R.ok().put("taskManagementPlanApprovalTable", planApprovalTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:save")
    public R save(@RequestBody PlanApprovalTableEntity planApprovalTable){
		planApprovalTableService.save(planApprovalTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:update")
    public R update(@RequestBody PlanApprovalTableEntity planApprovalTable){
		planApprovalTableService.updateById(planApprovalTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("generator:taskmanagementplanapprovaltable:delete")
    public R delete(@RequestBody Long[] approvalIds){
		planApprovalTableService.removeByIds(Arrays.asList(approvalIds));
        return R.ok();
    }

}
