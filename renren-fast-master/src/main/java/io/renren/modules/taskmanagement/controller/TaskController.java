package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("taskmanagement/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

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
