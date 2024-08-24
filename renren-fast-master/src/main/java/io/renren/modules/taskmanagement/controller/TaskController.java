package io.renren.modules.taskmanagement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.util.DateUtils;
import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.taskmanagement.entity.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequiresPermissions("taskmanagement:task:list")
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
    @RequestMapping("/executeTask/{taskId}")
//    @GetMapping("/executeTask/{taskId}")
    public R executeTask(@PathVariable("taskId") Long taskId){
        log.info("当前任务id为：" + taskId);
//        TaskEntity taskEntity = taskService.getOne(new QueryWrapper<>())

        TaskEntity taskEntity = taskService.getByTaskId(taskId);
//        Integer taskCurrentState = taskEntity.getTaskCurrentState();
//        log.info("当前任务状态为：" + taskCurrentState);
//        if (taskCurrentState.equals(1)){
//            taskEntity.setTaskCurrentState(2);
//        }

        if (taskEntity.getTaskCurrentState() == TaskStatus.IN_PROGRESS){
            taskEntity.setTaskCurrentState(TaskStatus.APPROVAL_IN_PROGRESS);
        } else {
            return R.error("当前任务状态不是进行中，不能进行审核");
        }

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
        
        taskService.updateById(taskEntity);
        
        return R.ok();

    }
    
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

}
