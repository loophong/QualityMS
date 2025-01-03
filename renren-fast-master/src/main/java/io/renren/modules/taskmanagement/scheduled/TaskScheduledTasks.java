package io.renren.modules.taskmanagement.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.app.service.UserService;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.service.MessageNotificationService;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import io.renren.modules.taskmanagement.service.PlanService;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TaskScheduledTasks {
    @Autowired
    private PlanService planService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MessageNotificationService messageService;
    @Autowired
    private UserService userService;


    //    @Scheduled(cron = "0 0 4 * * *")
    @Scheduled(cron = "0,30 * * * * *")
    public void taskScheduledTasks() {
        log.info("定时任务执行了");
        checkPlanIsCanStart();
        checkTaskIsCanStart();

        checkPlanIsOverdue();
        checkTaskIsOverdue();

        checkTaskIsCompleted();
        checkPlanIsCompleted();

        trackPlanProgress();
        trackTaskProgress();
    }

    /**
     * @description: 检查计划是否可以开始执行，如果当前时间 >= 计划开始时间，则修改计划状态为进行中
     * @author: hong
     * @date: 2024/8/27 13:38
     * @version: 1.0
     */
    public void checkPlanIsCanStart() {
        log.info("检查计划是否可以开始执行");
        planService.query().eq("plan_current_state", TaskStatus.NOT_STARTED).list().forEach(plan -> {
            log.info("检查计划是否可以开始执行,当前检查的计划为：" + plan);
            Date now = DateUtils.getNow();
            log.info("当前时间：" + now);
            // 判断当前时间是否大于等于计划开始时间
            if (plan.getPlanStartDate().getTime() <= now.getTime()) {
                log.info("计划" + plan.getPlanId() + "开始执行");
                plan.setPlanCurrentState(TaskStatus.IN_PROGRESS);
                planService.updateById(plan);
            }
            // 发送通知
            for (String s : plan.getPlanExecutor()) {
                log.info("发送通知的用户id：" + s);
                messageService.sendMessages(new CreateNoticeParams(
                        Long.parseLong(plan.getPlanInitiator()) ,
                        new Long[]{Long.valueOf(s)},
                        "您的计划" + plan.getPlanName() + "已开始执行",
                        "计划开始通知",
                        "plan_page"
                ));
            }

        });
    }

    /**
     * @description: 检查任务是否可以开始执行，如果当前时间 >= 计划任务时间，则修改计划状态为进行中
     * @author: hong
     * @date: 2024/8/27 13:38
     * @version: 1.0
     */
    public void checkTaskIsCanStart() {
        log.info("检查任务是否可以开始执行");
        taskService.query().eq("task_current_state", TaskStatus.NOT_STARTED).list().forEach(task -> {
            log.info("检查任务是否可以开始执行,当前检查的任务为：" + task);
            Date now = DateUtils.getNow();
            log.info("当前时间：" + now);
            // 判断当前时间是否大于等于计划开始时间
            if (task.getTaskStartDate().getTime() <= now.getTime()) {
                log.info("任务" + task.getTaskId() + "开始执行");
                task.setTaskCurrentState(TaskStatus.IN_PROGRESS);
                taskService.updateById(task);
            }
            // 发送通知
            for (String s : task.getTaskExecutor()) {
                messageService.sendMessages(new CreateNoticeParams(
                        Long.parseLong(task.getTaskPrincipal()) ,
                        new Long[]{Long.valueOf(s)},
                        "您的任务" + task.getTaskName() + "已开始执行",
                        "任务执行通知",
                        "task_page"
                ));
            }
        });
    }

    /**
     * @description: 检查计划是否超期
     * @author: hong
     * @date: 2024/8/27 14:04
     * @version: 1.0
     */
    public void checkPlanIsOverdue() {
        planService.query().eq("plan_current_state", TaskStatus.IN_PROGRESS).list().forEach(plan -> {
            log.info("检查计划是否超期,当前检查的计划为：" + plan);
            Date now = DateUtils.getNow();
            log.info("当前时间：" + now);
            // 判断计划预计完成时间是否小于等于当前时间
            if (plan.getPlanScheduleCompletionDate().getTime() <= now.getTime()) {
                log.info("计划" + plan.getPlanId() + "超期");
                plan.setPlanIsOverdue(1);
                planService.updateById(plan);
            }

        });
    }

    /**
     * @description: 检查任务是否超期
     * @author: hong
     * @date: 2024/8/27 14:04
     * @version: 1.0
     */
    public void checkTaskIsOverdue() {
        taskService.query().eq("task_current_state", TaskStatus.IN_PROGRESS).list().forEach(task -> {
            log.info("检查任务是否超期,当前检查的任务为：" + task);
            Date now = DateUtils.getNow();
            log.info("当前时间：" + now);
            // 判断计划预计完成时间是否小于等于当前时间

            Date zeroTime = DateUtils.getZeroTime();
            log.info("零点时间：" + zeroTime);

            if (task.getTaskScheduleCompletionDate().getTime() < zeroTime.getTime()) {
                log.info("任务" + task.getTaskId() + "超期");
                task.setTaskIsOverdue(1L);

                task.setTaskLagDays((zeroTime.getTime() - task.getTaskScheduleCompletionDate().getTime()) / 1000 / 60 / 60 / 24);

                taskService.updateById(task);
            } else {
                task.setTaskIsOverdue(0L);
                task.setTaskLagDays(0L);
                taskService.updateById(task);

            }

        });
    }

    /**
     * @description: 检查当前任务是否完成，如果当前任务其子任务全部完成，则修改任务状态  未完成 ==> 已完成
     * @author: hong
     * @date: 2024/9/23 16:42
     * @version: 1.0
     */
    public void checkTaskIsCompleted() {
        taskService.query().eq("task_current_state", TaskStatus.IN_PROGRESS).list().forEach(task -> {
            log.info("检查任务是否完成，当前检查的任务为：" + task);

            List<TaskEntity> childrenTasks = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", task.getTaskId()));
            if (childrenTasks.size() > 0) {
                for (TaskEntity childrenTask : childrenTasks) {
                    if (childrenTask.getTaskCurrentState() != TaskStatus.COMPLETED) {
                        return;
                    }
                }


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
                taskService.updateById(task);
            }

        });
    }

    /**
     * @description: 检查当前计划是否完成，如果当前计划其子任务全部完成，则修改计划状态  未完成 ==> 已完成
     * @author: hong
     * @date: 2024/9/23 16:42
     * @version: 1.0
     */
    public void checkPlanIsCompleted() {
        planService.query().eq("plan_current_state", TaskStatus.IN_PROGRESS).list().forEach(plan -> {
            log.info("检查计划是否完成,当前检查的计划为：" + plan);

            List<TaskEntity> childrenTasks = taskService.list(new QueryWrapper<TaskEntity>().eq("task_associated_plan_id", plan.getPlanId()));
            if (childrenTasks.size() > 0) {
                for (TaskEntity childrenTask : childrenTasks) {
                    if (childrenTask.getTaskCurrentState() != TaskStatus.COMPLETED) {
                        return;
                    }
                }
                Date zeroTime = DateUtils.getZeroTime();
                plan.setPlanActualCompletionDate(zeroTime);
                plan.setPlanActualDays((zeroTime.getTime() - plan.getPlanStartDate().getTime()) / 1000 / 60 / 60 / 24);
                plan.setPlanCurrentState(TaskStatus.COMPLETED);
                plan.setPlanIsCompleted(1);
                if (plan.getPlanScheduleDays() >= plan.getPlanActualDays()){
                    plan.setPlanEarlyCompletionDays(plan.getPlanScheduleDays() - plan.getPlanActualDays());
                }
                planService.updateById(plan);
            }

        });
    }

    /**
     * @description: 统计计划进度
     * @author: hong
     * @date: 2024/9/25 18:43
     * @version: 1.0
     */
    public void trackPlanProgress() {
        planService.query().list().forEach(plan -> {
            log.info("统计计划进度,当前检查的计划为：" + plan);

            //查询计划的直属子任务
            int allChildNum = taskService.list(new QueryWrapper<TaskEntity>().eq("task_associated_plan_id", plan.getPlanId())
                    .and(w -> w.eq("task_parent_node", plan.getPlanId()))).size();
            int allChildFinishedNum = taskService.list(new QueryWrapper<TaskEntity>().eq("task_associated_plan_id", plan.getPlanId())
                    .and(w -> w.eq("task_parent_node", plan.getPlanId()))
                    .and(w -> w.eq("task_current_state", TaskStatus.COMPLETED))).size();

            log.info("allChildNum:" + allChildNum);
            log.info("allChildFinishedNum:" + allChildFinishedNum);

            plan.setPlanSchedule(String.valueOf((int) (allChildFinishedNum * 100.0 / allChildNum)));
            planService.updateById(plan);
        });
    }

    /**
     * @description: 统计任务进度
     * @author: hong
     * @date: 2024/9/25 18:43
     * @version: 1.0
     */
    public void trackTaskProgress() {
        taskService.query().list().forEach(task -> {
            log.info("统计计划进度,当前检查的计划为：" + task);

            //查询计划的直属子任务
            int allChildNum = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", task.getTaskId())).size();
            int allChildFinishedNum = taskService.list(new QueryWrapper<TaskEntity>().eq("task_parent_node", task.getTaskId())
                    .and(w -> w.eq("task_current_state", TaskStatus.COMPLETED))).size();

            log.info("allChildNum:" + allChildNum);
            log.info("allChildFinishedNum:" + allChildFinishedNum);

            if (allChildNum == 0 || allChildFinishedNum == 0) {
                task.setTaskSchedule(String.valueOf(0));
            } else {
                task.setTaskSchedule(String.valueOf((int) (allChildFinishedNum * 100.0 / allChildNum)));
            }
            taskService.updateById(task);

        });
    }


}
