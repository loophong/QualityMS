package io.renren.modules.taskmanagement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import com.alibaba.excel.util.DateUtils;
import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.service.MessageNotificationService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.taskmanagement.dto.TaskQueryParamDTO;
import io.renren.modules.taskmanagement.dto.TaskSubmitApprovalDTO;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.ApprovalFileService;
import io.renren.modules.taskmanagement.service.ApprovalService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private MessageNotificationService messageService;
    @Autowired
    private ApprovalFileService approvalFileService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @description: 图中点击任务时，查询任务的全部信息和审批文件
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/12/11 17:00
     */
    @PostMapping("/taskAllInfo")
    public R taskAllInfo(@RequestBody String param) throws Exception {
        log.info("param:" + param);
        Map<String, Object> jsonMap = objectMapper.readValue(param, Map.class);
        String taskId = (String) jsonMap.get("taskId");
        log.info("taskId:" + taskId);
        TaskEntity task = taskService.query().eq("task_id", taskId).one();
        // 检查task状态，如果通过则查询审批文件
        if (task.getTaskCurrentState() != TaskStatus.COMPLETED) {
            return R.ok().put("task", task).put("approval", null).put("fileList", null);
        }

        // 查询审批编号
        ApprovalEntity approval = approvalService.query()
                .eq("task_id", taskId)
                .eq("approval_status", ApprovalStatus.APPROVED)
                .orderByDesc("task_submission_time") // 按照 submit_time 降序排序
                .last("limit 1") // 限制结果为一条记录
                .one();// 获取一条记录
        // 检查是否有审批文件
        List<ApprovalFileEntity> fileList = approvalFileService.list(new QueryWrapper<ApprovalFileEntity>()
                .eq("approval_id", approval.getApprovalId()));
        log.info("approval" + approval);


        return R.ok().put("task", task).put("approval", approval).put("fileList", fileList.size() > 0 ? fileList : null);

    }

    /**
     * @description: 任务折线图展示
     * @author: hong
     * @date: 2024/9/25 16:52
     * @version: 1.0
     */
    @GetMapping("/taskLineDisplay")
    public List<TaskLineChartDTO> taskLineDisplay(String planId) {
        List<TaskLineChartDTO> taskLineChartDTOS = taskService.taskLineDisplay(planId);
        log.info("taskLineChartDTOS" + taskLineChartDTOS);
        return taskLineChartDTOS;
    }

    /**
     * @description: 任务统计
     * @author: hong
     * @date: 2024/9/23 17:50
     * @version: 1.0
     */
    @GetMapping("/taskStatistics")
    public TaskStatisticsDTO taskStatistics(String planId) {
        return taskService.taskStatistics(planId);
    }


    /**
     * @description: 小屏问题，任务进度，与任务关联的指标中，显示指标的已完成和未完成数
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/28 14:49
     */
    @RequestMapping("/statisticsOnTaskRelatedIndicators")
    public R statisticsOnTaskRelatedIndicators() {
        List<TaskEntity> unfinishedList = taskService.list(
                new QueryWrapper<TaskEntity>()
                        .isNotNull("task_associated_indicators_id").ne("task_associated_indicators_id", "")
                        .eq("task_is_completed", 0));
        List<TaskEntity> finishedList = taskService.list(
                new QueryWrapper<TaskEntity>()
                        .isNotNull("task_associated_indicators_id").ne("task_associated_indicators_id", "")
                        .eq("task_is_completed", 1));
        log.info("未完成的任务数量为：" + unfinishedList);
        log.info("未完成的任务数量为：" + unfinishedList.size());
        log.info("已完成的任务数量为：" + finishedList);
        log.info("已完成的任务数量为：" + finishedList.size());
        // 将上面两个数封装成json返回给前端
        return R.ok().put("unfinishedNum", unfinishedList.size() > 0 ? unfinishedList.size() : 0).put("finishedNum", finishedList.size() > 0 ? finishedList.size() : 0);
    }


    /**
     * @description: 小屏问题，指标任务数
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/28 15:01
     */
    @RequestMapping("/countTaskRelatedIndicatorsNum")
    public R countTaskRelatedIndicatorsNum() {
        List<TaskEntity> list = taskService.list(
                new QueryWrapper<TaskEntity>()
                        .isNotNull("task_associated_indicators_id")
                        .ne("task_associated_indicators_id", ""));

        log.info("指标任务数：" + list);
        log.info("指标任务数：" + list.size());

        return R.ok().put("indicatorRelatedTaskNum", list.size() > 0 ? list.size() : 0);
    }


    /**
     * @description: 检查当前taskId, 任务编号是否被使用
     * @author: hong
     * @date: 2024/8/26 15:37
     * @version: 1.0
     */
    @GetMapping("/checkTaskId")
    public ResponseEntity<Boolean> checkPlanNumber(@RequestParam String taskId) {
        boolean isUsed = taskService.isTaskIdUsed(taskId);
        log.info("当前taskId" + taskId + ",是否使用" + isUsed);
        return ResponseEntity.ok(isUsed);
    }


    /**
     * @description: 查询当前任务，以及其子任务
     * @author: hong
     * @date: 2024/8/26 10:37
     * @version: 1.0
     */
    @RequestMapping("/getTaskDetailInfo")
    public R getTaskDetailInfo(@RequestParam String taskId) {
        log.info("taskId" + taskId);
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
    public R decompositionTask(@RequestBody TaskDetailDTO taskDetailDTO) {
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
    @RequestMapping("/getAllTasksByPlanId")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getAllTasksByPlanId(@RequestParam Map<String, Object> params) {
        PageUtils page = taskService.queryPageSelectTasksByPlanId(params, params.get("planId").toString());

        return R.ok().put("page", page);
    }


    /**
     * @description: 提交审批
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/24 18:50
     */
    @Transactional
    @PostMapping("/submitApprover")
    //    @RequiresPermissions("taskmanagement:task:list")
    public R approval(@RequestBody TaskSubmitApprovalDTO taskSubmitApprovalDTO) {
        log.info("提交审批" + taskSubmitApprovalDTO);

        String taskId = taskSubmitApprovalDTO.getTaskId();
        TaskEntity task = taskService.getByTaskId(taskId);

        // 审批之前先检查其子任务是否全部完成
        // 查询当前任务的子任务
        List<TaskEntity> childrenTasks = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", taskId));
        if (childrenTasks.size() > 0) {
            for (TaskEntity childrenTask : childrenTasks) {
                if (childrenTask.getTaskCurrentState() != TaskStatus.COMPLETED) {
                    return R.error("当前任务子任务未全部完成，不能进行审批");
                }
            }
        }

        if (task.getTaskCurrentState() == TaskStatus.IN_PROGRESS) {
            ApprovalEntity approvalEntity = new ApprovalEntity();
            approvalEntity.setApprovalContent(taskSubmitApprovalDTO.getApprovalContent());
            approvalEntity.setTaskId(taskId);
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
            approvalEntity.setApprover(taskSubmitApprovalDTO.getApprovalor());
            approvalEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));
            approvalService.save(approvalEntity);
            // 设置当前任务的状态为审批中
            task.setTaskCurrentState(TaskStatus.APPROVAL_IN_PROGRESS);
            taskService.updateById(task);

            // 保存文件信息
            for (FileEntity file : taskSubmitApprovalDTO.getFiles()) {
                ApprovalFileEntity fileEntity = new ApprovalFileEntity();
                fileEntity.setTaskId(taskId);
                fileEntity.setName(file.getName());
                fileEntity.setUrl(file.getUrl());
                fileEntity.setApprovalId(approvalEntity.getApprovalId());
                approvalFileService.save(fileEntity);
            }

        } else {
            return R.error("当前任务状态不是进行中，不能进行审核");
        }

////        taskService.updateById(taskEntity);
//
        // 发送消息
        messageService.sendMessages(new CreateNoticeParams(
                ShiroUtils.getUserId(),
                new Long[]{Long.parseLong(task.getTaskAuditor())},
                "您有一个任务需要审批，请及时审批！",
                "任务审批通知",
                "task_approval_page"));
//
        return R.ok();

    }
//    @Transactional
//    @RequestMapping("/submitApprover")
//    //    @RequiresPermissions("taskmanagement:task:list")
//    public R executeTask(String taskId, String taskApprovalor) {
//
//        log.info("当前任务id为：" + taskId);
//        TaskEntity task = taskService.getByTaskId(taskId);
//
//        // 审批之前先检查其子任务是否全部完成
//        // 查询当前任务的子任务
//        List<TaskEntity> childrenTasks = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", taskId));
//        if (childrenTasks.size() > 0) {
//            for (TaskEntity childrenTask : childrenTasks) {
//                if (childrenTask.getTaskCurrentState() != TaskStatus.COMPLETED) {
//                    return R.error("当前任务子任务未全部完成，不能进行审批");
//                }
//            }
//        }
//
//        if (task.getTaskCurrentState() == TaskStatus.IN_PROGRESS) {
//            ApprovalEntity approvalEntity = new ApprovalEntity();
//            approvalEntity.setTaskId(taskId);
//            approvalEntity.setTaskName(task.getTaskName());
//            approvalEntity.setTaskContent(task.getTaskContent());
//            approvalEntity.setTaskAssociatedPlanId(task.getTaskAssociatedPlanId());
//            log.info("当前任务的task信息为：" + task);
//
//            approvalEntity.setTaskPrincipal(task.getTaskPrincipal());
//
//            log.info("当前任务的AssociatedIndicatorsId为：" + task.getTaskAssociatedIndicatorsId());
//            if (task.getTaskAssociatedIndicatorsId() != null && !task.getTaskAssociatedIndicatorsId().equals("")) {
//                approvalEntity.setTaskAssociatedIndicatorsId(task.getTaskAssociatedIndicatorsId());
//            }
//            //获取当前时间
//
//            approvalEntity.setTaskStartDate(task.getTaskStartDate());
//            approvalEntity.setTaskSubmissionTime(new Date());
////            approvalEntity.setTaskStartDate(task.getTaskStartDate());
//            approvalEntity.setApprover(taskApprovalor);
//            approvalEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));
//
//
//            task.setTaskCurrentState(TaskStatus.APPROVAL_IN_PROGRESS);
//
//            taskService.updateById(task);
//            approvalService.save(approvalEntity);
//        } else {
//            return R.error("当前任务状态不是进行中，不能进行审核");
//        }
////
////
////        taskService.updateById(taskEntity);
//
//        // 发送消息
//        messageService.sendMessages(new CreateNoticeParams(Long.parseLong(taskApprovalor), new Long[]{ShiroUtils.getUserId()}, "您有一个任务需要审批，请及时审批！", "任务审批通知"));
//
//        return R.ok();
//
//    }

    /**
     * @description: 测试获取当前登录的用户信息用户信息
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/8/21 18:08
     */
    @RequestMapping("/testUserInfo")
//    @RequiresPermissions("taskmanagement:task:list")
    public R userInfo(@RequestParam Map<String, Object> params) {
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
    public R selectTasksByUserId(@RequestParam Map<String, Object> params) {
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
    @PostMapping("/getUnfinishedTasksList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getUnfinishedTasksList(@RequestBody TaskQueryParamDTO taskQueryParamDTO) {
        log.info("当前用户未完成的任务参数为：" + taskQueryParamDTO);
        PageUtils page = taskService.queryPageGetUnfinishedTasks(taskQueryParamDTO, ShiroUtils.getUserId());
        log.info("当前用户未完成的任务为：" + page.getList().toString());
        return R.ok().put("page", page);
    }
//    @RequestMapping("/getUnfinishedTasksList")
//    @RequiresPermissions("taskmanagement:task:list")
//    public R getUnfinishedTasksList(@RequestParam Map<String, Object> params){
//        PageUtils page = taskService.queryPageGetUnfinishedTasks(params,ShiroUtils.getUserId());
//        log.info("当前用户未完成的任务为：" + page.getList().toString());
//        return R.ok().put("page", page);
//    }

    /**
     * @description: 获取当前用户，已完成任务
     * @author: hong
     * @date: 2024/8/23 15:53
     */
    @PostMapping("/getCompletedTasksList")
//    @RequiresPermissions("taskmanagement:task:list")
    public R getCompletedTasksList(@RequestBody TaskQueryParamDTO taskQueryParamDTO) {
        log.info("当前用户完成的任务参数为：" + taskQueryParamDTO);
        PageUtils page = taskService.queryPageGetCompletedTasksList(taskQueryParamDTO, ShiroUtils.getUserId());
        return R.ok().put("page", page);
    }
//    @RequestMapping("/getCompletedTasksList")
////    @RequiresPermissions("taskmanagement:task:list")
//    public R getCompletedTasksList(@RequestParam Map<String, Object> params){
//        PageUtils page = taskService.queryPageGetCompletedTasksList(params,ShiroUtils.getUserId());
//        log.info("当前用户已完成的任务为：" + page);
//        return R.ok().put("page", page);
//    }

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
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = taskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tmTid}")
    @RequiresPermissions("taskmanagement:task:info")
    public R info(@PathVariable("tmTid") Long tmTid) {
        TaskEntity taskManagementTask = taskService.getById(tmTid);

        return R.ok().put("taskManagementTask", taskManagementTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("taskmanagement:task:save")
    public R save(@RequestBody TaskEntity taskManagementTask) {
        taskService.save(taskManagementTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:task:update")
    public R update(@RequestBody TaskEntity taskManagementTask) {
        taskService.updateById(taskManagementTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:task:delete")
    public R delete(@RequestBody Long[] tmTids) {
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
    public R deleteByTaskId(@RequestParam Long tmTid) {
        taskService.removeById(tmTid);
        return R.ok();
    }

}
