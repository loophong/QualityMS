package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.service.MessageNotificationService;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.ApprovalFileService;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.taskmanagement.service.ApprovalService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.annotation.Resource;


/**
 * 任务审批表
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-23 09:58:46
 */
@Slf4j
@RestController
@RequestMapping("taskmanagement/approval")
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MessageNotificationService messageService;

    @Resource
    private ApprovalFileService approvalFileService;

    /**
     * @description: 取消审批
     * @author: hong
     * @date: 2024/8/30 17:17
     * @version: 1.0
     */
    @GetMapping("/cancelApproval")
    @Transactional
//    @RequiresPermissions("taskmanagement:approval:cancelApproval")
    public R cancelApproval(@RequestParam String taskId) {

        log.info("取消审批" + taskId);

        //检查任务是否存在
        if (taskService.getByTaskId(taskId) == null) {
            return R.error("任务不存在");
        }

        // 通过任务id获取任务信息
        TaskEntity task = taskService.getByTaskId(taskId);
        if (task.getTaskCurrentState() != TaskStatus.APPROVAL_IN_PROGRESS) {
            return R.error("当前任务状态不允许取消审批");
        } else {
            task.setTaskCurrentState(TaskStatus.IN_PROGRESS);
        }

        // 通过任务id获取审批信息
        ApprovalEntity approvalEntity = approvalService.query().eq("task_id", taskId).eq("approval_status", ApprovalStatus.PENDING).one();
        if (approvalEntity != null) {
            approvalEntity.setApprovalStatus(ApprovalStatus.CANCEL);
        } else {
            return R.error("当前任务不存在审批信息");
        }

        taskService.updateById(task);
        approvalService.updateById(approvalEntity);

        return R.ok();
    }

    /**
     * @description: 获取我提交的审批getMySubmitApprovalList
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/25 13:28
     */
    @RequestMapping("/getMySubmitApprovalList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getMySubmitApprovalList(@RequestParam Map<String, Object> params) {

        PageUtils page = approvalService.queryPageGetMySubmitApprovalList(params, ShiroUtils.getUserId());

        return R.ok().put("page", page);

    }

    /**
     * @description: 查询我的审批全部列表
     * @param:
     * @return:
     * @author: hong
     * @date: 2024/8/23 10:26
     */
    @RequestMapping("/myApprovalList")
//    @RequiresPermissions("taskmanagement:approval:list")
    public R myApprovalList(@RequestParam Map<String, Object> params) {
        log.info("查询我的审批列表" + ShiroUtils.getUserId());
        PageUtils page = approvalService.queryPageMyApprovalList(params, ShiroUtils.getUserId());

        return R.ok().put("page", page);
    }

    /**
     * @description: 查询待我审批列表
     * @author: hong
     * @date: 2024/8/23 10:26
     */
    @RequestMapping("/pendingApprovalsList")
//    @RequiresPermissions("taskmanagement:approval:list")
    public R getPendingApprovalsList(@RequestParam Map<String, Object> params) {
        PageUtils page = approvalService.queryPagePendingApprovalsList(params, ShiroUtils.getUserId());
        log.info("查询待审批列表" + page);

        return R.ok().put("page", page);
    }

    /**
     * @description: 查询已审批列表
     * @author: hong
     * @date: 2024/8/23 10:26
     */
    @RequestMapping("/historyApprovalList")
//    @RequiresPermissions("taskmanagement:approval:list")
    public R getHistoryApprovalList(@RequestParam Map<String, Object> params) {
        PageUtils page = approvalService.queryPageHistoryApprovalList(params, ShiroUtils.getUserId());
        log.info("查询已审批列表" + page);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:approval:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = approvalService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{approvalId}")
    @RequiresPermissions("taskmanagement:approval:info")
    public R info(@PathVariable("approvalId") Long approvalId) {
        ApprovalEntity taskManagementApprovalTable = approvalService.getById(approvalId);
        List<ApprovalFileEntity> files = approvalFileService.query().eq("approval_id", approvalId).list();
        if (files != null) {
            return R.ok().put("taskManagementApprovalTable", taskManagementApprovalTable).put("files", files);
        } else {
            return R.ok().put("taskManagementApprovalTable", taskManagementApprovalTable);
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:approval:save")
    public R save(@RequestBody ApprovalEntity taskManagementApprovalTable) {
        approvalService.save(taskManagementApprovalTable);

        return R.ok();
    }

    /**
     * @description: 审批
     * @author: hong
     * @date: 2024/8/30 16:39
     * @version: 1.0
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:approval:update")
    public R update(@RequestBody ApprovalEntity taskManagementApprovalTable) {

        // 当前任务提交审批前，先检查其下级任务是否已经完成
//        if (taskManagementApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED) {
//            if (taskService.query().eq("task_parent_node", taskManagementApprovalTable.getTaskId()).eq("task_current_state", TaskStatus.COMPLETED).count() == 0) {
//                return R.error("请先完成下级任务");
//            }
//            if (taskService.query().eq("task_parent_node", taskManagementApprovalTable.getTaskId()).notIn("task_current_state", TaskStatus.COMPLETED).count() > 0) {
//                return R.error("请先完成下级任务");
//            }
//        }

        //检查任务是否存在
        if (taskService.getByTaskId(taskManagementApprovalTable.getTaskId()) == null) {
            return R.error("任务不存在");
        }
        TaskEntity task = taskService.getByTaskId(taskManagementApprovalTable.getTaskId());
        if (taskManagementApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED) {
            // 如果审批通过，则将任务状态改为已完成，并且记录相关信息
            task.setTaskCurrentState(TaskStatus.COMPLETED);
            task.setTaskIsCompleted(1L);
            // 项目实际完成时间
            Date now = DateUtils.getNow();
            task.setTaskActualCompletionDate(now);
            // 项目实际完成天数
            long completionDays = (DateUtils.getZeroTime().getTime() - task.getTaskStartDate().getTime()) / (1000 * 60 * 60 * 24);
            task.setTaskActualDays(completionDays);

            // 任务是否超期,设置超期时间
            if (DateUtils.getZeroTime().getTime() - task.getTaskScheduleCompletionDate().getTime() > 0) {
                task.setTaskIsOverdue(1L);
                task.setTaskLagDays((DateUtils.getZeroTime().getTime() - task.getTaskScheduleCompletionDate().getTime()) / (1000 * 60 * 60 * 24));
            } else {
                task.setTaskIsOverdue(0L);
                task.setTaskLagDays(0L);
            }

            // 任务是否按时完工
            if (DateUtils.getZeroTime().getTime() == task.getTaskScheduleCompletionDate().getTime()) {
                task.setTaskIsOnTime(0L);
            } else {
                task.setTaskIsOnTime(1L);
            }

            // 任务是否提前完工
            if (DateUtils.getZeroTime().getTime() - task.getTaskScheduleCompletionDate().getTime() < 0) {
                task.setTaskEarlyCompletionDays((task.getTaskScheduleCompletionDate().getTime() - DateUtils.getZeroTime().getTime()) / (1000 * 60 * 60 * 24));
            } else {
                task.setTaskEarlyCompletionDays(0L);
            }

        } else {
            task.setTaskCurrentState(TaskStatus.IN_PROGRESS);
        }
        taskService.updateById(task);
        approvalService.updateById(taskManagementApprovalTable);

        // 发送消息
        messageService.sendMessages(new CreateNoticeParams(Long.parseLong(taskManagementApprovalTable.getApprover()), new Long[]{Long.valueOf(taskManagementApprovalTable.getSubmitter())},
                "您有一个任务审批结果，请及时查看！", "任务审批结果通知", "task_page"));
        return R.ok();
    }


    /**
     * @description: 审批
     * @author: hong
     * @date: 2024/8/30 16:39
     * @version: 1.0
     */
    @RequestMapping("/approval")
    @RequiresPermissions("taskmanagement:approval:update")
    public R approval(@RequestBody ApprovalEntity taskManagementApprovalTable) {

        //检查任务是否存在
        if (taskService.getByTaskId(taskManagementApprovalTable.getTaskId()) == null) {
            return R.error("任务不存在");
        }
        TaskEntity task = taskService.getByTaskId(taskManagementApprovalTable.getTaskId());
        if (taskManagementApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED) {
            // 如果审批通过，则将任务状态改为已完成，并且记录相关信息
            task.setTaskCurrentState(TaskStatus.NOT_STARTED);
            task.setTaskIsCompleted(0L);
            // 项目实际完成时间
            task.setTaskActualCompletionDate(null);
            // 项目实际完成天数
            task.setTaskActualDays(null);

            // 任务是否超期,设置超期时间
            task.setTaskIsOverdue(0L);
            task.setTaskLagDays(0L);

            // 任务是否按时完工
            task.setTaskIsOnTime(null);

            // 任务是否提前完工
            task.setTaskEarlyCompletionDays(0L);

        } else {
            if (taskManagementApprovalTable.getApprovalType().equals("NEW")) {
                task.setTaskCurrentState(TaskStatus.PREAPPROVAL_NOT_PASSED);
            } else if (taskManagementApprovalTable.getApprovalType().equals("UPDATE")) {
                task.setTaskCurrentState(TaskStatus.UPDATE_APPROVAL_NOT_PASSED);
            }
        }
        taskService.updateById(task);
        approvalService.updateById(taskManagementApprovalTable);

        // 发送消息
        messageService.sendMessages(new CreateNoticeParams(Long.parseLong(taskManagementApprovalTable.getApprover()), new Long[]{Long.valueOf(taskManagementApprovalTable.getSubmitter())},
                "您有一个任务审批结果，请及时查看！", "任务审批结果通知", "task_page"));
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:approval:delete")
    public R delete(@RequestBody Long[] approvalIds) {
        approvalService.removeByIds(Arrays.asList(approvalIds));

        return R.ok();
    }

}
