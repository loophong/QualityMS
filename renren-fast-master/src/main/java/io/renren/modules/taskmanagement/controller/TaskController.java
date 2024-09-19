package io.renren.modules.taskmanagement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import com.alibaba.excel.util.DateUtils;
import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.taskmanagement.entity.ApprovalEntity;
import io.renren.modules.taskmanagement.entity.TaskDetailDTO;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import io.renren.modules.taskmanagement.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.service.TaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 任务管理模块任务表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Slf4j
@RestController
@RequestMapping("taskmanagement/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ApprovalService approvalService;



    /**
     * @description: 检查当前taskId,任务编号是否被使用
     * @author: hong
     * @date: 2024/8/26 15:37
     * @version: 1.0
     */
    @GetMapping("/checkTaskId")
    public ResponseEntity<Boolean> checkPlanNumber(@RequestParam String taskId) {
        boolean isUsed = taskService.isTaskIdUsed(taskId);
        log.info("当前taskId"+taskId+",是否使用"+isUsed);
        return ResponseEntity.ok(isUsed);
    }


    /**
     * @description: 查询当前任务，以及其子任务
     * @author: hong
     * @date: 2024/8/26 10:37
     * @version: 1.0
     */
    @RequestMapping("/getTaskDetailInfo")
    public R getTaskDetailInfo(@RequestParam String taskId){
        log.info("taskId"+taskId);
        TaskDetailDTO taskDetailDTO = taskService.getTaskDetailInfo(taskId);

        return R.ok().put("taskDetail", taskDetailDTO);
    }

    /**
     * @description: 任务分解
     * @author: hong
     * @date: 2024/8/26 14:35
     * @version: 1.0
     */
    @PostMapping("/decomposition")
//    @RequiresPermissions("taskmanagement:task:list")
    public R decompositionTask(@RequestBody TaskDetailDTO taskDetailDTO){
        log.info("当前分解的任务详情为：" + taskDetailDTO);
        int i = taskService.saveDecompositionTasks(taskDetailDTO.getTasks());
        return R.ok();

    }




    /**
     * @description: 通过planId，查询属于该id的全部任务
     * @param: planId
     * @author: hong
     * @date: 2024/8/25 16:08
     */
//    @RequestMapping("/getAllTasksByPlanId")
////    @RequiresPermissions("taskmanagement:task:list")
//    public R getAllTasksByPlanId(String planId){
//
//        taskService.selectAllTasksByPlanId(planId);
//        log.info("当前用户的任务为：" + page);
//
//
//        return R.ok().put("page", page);
//
//    }




    /**
     * @description: 提交审批
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/24 18:50
     */

    @RequestMapping("/submitApprover")
    //    @RequiresPermissions("taskmanagement:task:list")
    public R executeTask(String taskId,String taskApprovalor){

        log.info("当前任务id为：" + taskId);
        TaskEntity task = taskService.getByTaskId(taskId);

        // 审批之前先检查其子任务是否全部完成
        // 查询当前任务的子任务
        List<TaskEntity> childrenTasks = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", taskId));
        if (childrenTasks.size() > 0){
            for (TaskEntity childrenTask : childrenTasks) {
                if (childrenTask.getTaskCurrentState() != TaskStatus.COMPLETED){
                    return R.error("当前任务子任务未全部完成，不能进行审批");
                }
            }
        }

        if (task.getTaskCurrentState() == TaskStatus.IN_PROGRESS){
            ApprovalEntity approvalEntity = new ApprovalEntity();
            approvalEntity.setTaskId(taskId);
            approvalEntity.setTaskName(task.getTaskName());
            approvalEntity.setTaskContent(task.getTaskContent());
            approvalEntity.setTaskAssociatedPlanId(task.getTaskAssociatedPlanId());
            approvalEntity.setTaskPrincipal(task.getTaskPrincipal());
            approvalEntity.setTaskAssociatedIndicatorsId(task.getTaskAssociatedIndicatorsId());
            //获取当前时间

            approvalEntity.setTaskStartDate(task.getTaskStartDate());
            approvalEntity.setTaskSubmissionTime(new Date());
//            approvalEntity.setTaskStartDate(task.getTaskStartDate());
            approvalEntity.setApprover(taskApprovalor);
            approvalEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));


            task.setTaskCurrentState(TaskStatus.APPROVAL_IN_PROGRESS);

            taskService.updateById(task);
            approvalService.save(approvalEntity);
        } else {
            return R.error("当前任务状态不是进行中，不能进行审核");
        }
//
//
//        taskService.updateById(taskEntity);

        return R.ok();

    }
    
    /** 
     * @description: 测试获取当前登录的用户信息用户信息
     * @param: null 
     * @return:  
     * @author: hong
     * @date: 2024/8/21 18:08
     */
    @RequestMapping("/testUserInfo")
//    @RequiresPermissions("taskmanagement:task:list")
    public R userInfo(@RequestParam Map<String, Object> params){
        SysUserEntity userEntity = ShiroUtils.getUserEntity();

        log.info("当前登录的用户信息为：" + userEntity);

        Long userId = ShiroUtils.getUserId();

        log.info("当前登录的用户id为：" + userId);

        return R.ok();

    }

    /**
     * @description: 查询当前用户全部任务
     * @param:
     * @return:
     * @author: hong
     * @date: 2024/8/21 19:38
     */
    @RequestMapping("/getTasksByUserId")
//    @RequiresPermissions("taskmanagement:task:list")
    public R selectTasksByUserId(@RequestParam Map<String, Object> params){
        SysUserEntity userEntity = ShiroUtils.getUserEntity();

        log.info("当前登录的用户信息为：" + userEntity);

        Long userId = ShiroUtils.getUserId();

        log.info("当前登录的用户id为：" + userId);

//        List<TaskEntity> tasks = taskService.selectTasksByUserId(userId);

//        log.info("当前用户的任务为：" + tasks);

        PageUtils page = taskService.queryPageSelectTasksByUserId(params, userId);
        log.info("当前用户的任务为：" + page);


        return R.ok().put("page", page);

    }

    /**
     * @description: 获取当前用户，未完成任务
     * @author: hong
     * @date: 2024/8/23 15:53
     */
    @RequestMapping("/getUnfinishedTasksList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getUnfinishedTasksList(@RequestParam Map<String, Object> params){
        PageUtils page = taskService.queryPageGetUnfinishedTasks(params,ShiroUtils.getUserId());
        log.info("当前用户未完成的任务为：" + page);
        return R.ok().put("page", page);
    }

    /**
     * @description: 获取当前用户，已完成任务
     * @author: hong
     * @date: 2024/8/23 15:53
     */
    @RequestMapping("/getCompletedTasksList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getCompletedTasksList(@RequestParam Map<String, Object> params){
        PageUtils page = taskService.queryPageGetCompletedTasksList(params,ShiroUtils.getUserId());
        log.info("当前用户已完成的任务为：" + page);
        return R.ok().put("page", page);
    }

    /**
     * @description: 修改任务状态，进行中--》审核中
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/21 19:46
     */
//    @RequestMapping("/executeTask")
//    @RequiresPermissions("taskmanagement:task:list")
//    @RequestMapping("/executeTask/{taskId}")
//    @GetMapping("/executeTask/{taskId}")
//    public R executeTask(@PathVariable("taskId") Long taskId){
//        log.info("当前任务id为：" + taskId);
//        TaskEntity taskEntity = taskService.getOne(new QueryWrapper<>())

//        TaskEntity taskEntity = taskService.getByTaskId(taskId);
//        Integer taskCurrentState = taskEntity.getTaskCurrentState();
//        log.info("当前任务状态为：" + taskCurrentState);
//        if (taskCurrentState.equals(1)){
//            taskEntity.setTaskCurrentState(2);
//        }

//        if (taskEntity.getTaskCurrentState() == TaskStatus.IN_PROGRESS){
//            taskEntity.setTaskCurrentState(TaskStatus.APPROVAL_IN_PROGRESS);
//        } else {
//            return R.error("当前任务状态不是进行中，不能进行审核");
//        }

//        LocalDate localDate = LocalDateTime.now().toLocalDate();
//        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

//        log.info("当前日期：" + date);
//        Date date = new Date();
//        Date oldDate = new Date();
        // 实际完成日期
//        taskEntity.setTaskActualCompletionDate(date);

        // 实际天数
//        long diffMillis = date.getTime() - taskEntity.getTaskStartDate().getTime();
//        long diffDays = diffMillis / (24 * 60 * 60 * 1000);
//        log.info("实际天数"+diffDays);
//        taskEntity.setTaskActualDays(diffDays);
        
//        taskService.updateById(taskEntity);
        
//        return R.ok();

//    }
    
    /** 
     * @description:
     * @param: null 
     * @return:  
     * @author: hong
     * @date: 2024/8/21 19:51
     */ 


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:task:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = taskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tmTid}")
    @RequiresPermissions("taskmanagement:task:info")
    public R info(@PathVariable("tmTid") Long tmTid){
		TaskEntity taskManagementTask = taskService.getById(tmTid);

        return R.ok().put("taskManagementTask", taskManagementTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:task:save")
    public R save(@RequestBody TaskEntity taskManagementTask){
        taskService.save(taskManagementTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:task:update")
    public R update(@RequestBody TaskEntity taskManagementTask){
        taskService.updateById(taskManagementTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:task:delete")
    public R delete(@RequestBody Long[] tmTids){
        taskService.removeByIds(Arrays.asList(tmTids));

        return R.ok();
    }

    /**
     * @description: 删除 通过taskId
     * @author: hong
     * @date: 2024/8/26 17:20
     */
    @RequestMapping("/deleteByTaskId")
//    @RequiresPermissions("taskmanagement:task:delete")
    public R deleteByTaskId(@RequestParam Long tmTid){
        taskService.removeById(tmTid);
        return R.ok();
    }

}
