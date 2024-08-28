package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.entity.ApprovalStatus;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.taskmanagement.entity.ApprovalEntity;
import io.renren.modules.taskmanagement.service.ApprovalService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



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

    /**
     * @description: 获取我提交的审批getMySubmitApprovalList
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/25 13:28
     */
    @RequestMapping("/getMySubmitApprovalList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getMySubmitApprovalList(@RequestParam Map<String, Object> params){

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
    public R myApprovalList(@RequestParam Map<String, Object> params){
        log.info("查询我的审批列表"+ShiroUtils.getUserId());
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
    public R getPendingApprovalsList(@RequestParam Map<String, Object> params){
        PageUtils page = approvalService.queryPagePendingApprovalsList(params, ShiroUtils.getUserId());
        log.info("查询待审批列表"+page);

        return R.ok().put("page", page);
    }

    /**
     * @description: 查询已审批列表
     * @author: hong
     * @date: 2024/8/23 10:26
     */
    @RequestMapping("/historyApprovalList")
//    @RequiresPermissions("taskmanagement:approval:list")
    public R getHistoryApprovalList(@RequestParam Map<String, Object> params){
        PageUtils page = approvalService.queryPageHistoryApprovalList(params, ShiroUtils.getUserId());
        log.info("查询已审批列表"+page);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:approval:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = approvalService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{approvalId}")
    @RequiresPermissions("taskmanagement:approval:info")
    public R info(@PathVariable("approvalId") Long approvalId){
		ApprovalEntity taskManagementApprovalTable = approvalService.getById(approvalId);

        return R.ok().put("taskManagementApprovalTable", taskManagementApprovalTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:approval:save")
    public R save(@RequestBody ApprovalEntity taskManagementApprovalTable){
        approvalService.save(taskManagementApprovalTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:approval:update")
    public R update(@RequestBody ApprovalEntity taskManagementApprovalTable){

        //检查任务是否存在

        if (taskService.getByTaskId(taskManagementApprovalTable.getTaskId()) == null){
            return R.error("任务不存在");
        }
        TaskEntity task = taskService.getByTaskId(taskManagementApprovalTable.getTaskId());
        if (taskManagementApprovalTable.getApprovalStatus() == ApprovalStatus.APPROVED){
            task.setTaskCurrentState(TaskStatus.COMPLETED);
        }else {
            task.setTaskCurrentState(TaskStatus.IN_PROGRESS);
        }
        taskService.updateById(task);
        approvalService.updateById(taskManagementApprovalTable);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:approval:delete")
    public R delete(@RequestBody Long[] approvalIds){
        approvalService.removeByIds(Arrays.asList(approvalIds));

        return R.ok();
    }

}
