package io.renren.modules.taskmanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.log.Log;
import io.renren.modules.taskmanagement.entity.PlanAndTaskDTO;
import io.renren.modules.taskmanagement.entity.TaskEntity;
import io.renren.modules.taskmanagement.service.TaskService;
import javafx.concurrent.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.service.PlanService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@RestController
@RequestMapping("taskmanagement/plan")
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private TaskService taskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:plan:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = planService.queryPage(params);

        System.out.println(page.getList().toString());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tmPid}")
    @RequiresPermissions("taskmanagement:plan:info")
    public R info(@PathVariable("tmPid") Long tmPid){
		PlanEntity taskManagementPlan = planService.getById(tmPid);

        return R.ok().put("taskManagementPlan", taskManagementPlan);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("taskmanagement:plan:save")
    public R save(@RequestBody PlanAndTaskDTO planAndTaskDTO){
        System.out.println(planAndTaskDTO);

        planService.save(planAndTaskDTO.getPlan());

        for (int i = 0; i < planAndTaskDTO.getTasks().size(); i++) {
            taskService.save(planAndTaskDTO.getTasks().get(i));
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:plan:update")
    public R update(@RequestBody PlanEntity taskManagementPlan){
        planService.updateById(taskManagementPlan);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("taskmanagement:plan:delete")
    public R delete(@RequestBody Long[] tmPids){
        planService.removeByIds(Arrays.asList(tmPids));

        return R.ok();
    }

    /**
     * @description: 检查当前planId是否被使用
     * @author: hong
     * @date: 2024/7/23 16:37
     **/
    @GetMapping("/checkPlanNumber")
    public ResponseEntity<Boolean> checkPlanNumber(@RequestParam String planId) {
        boolean isUsed = planService.isPlanNumberUsed(planId);
        return ResponseEntity.ok(isUsed);
    }

    /**
     * @description: 查询计划的全部信息和其全部任务
     * @author: hong
     * @date: 2024/7/23 23:15
     **/
    @GetMapping("/getPlanAllInfo")
    public PlanAndTaskDTO getPlanAllInfo(@RequestParam String planId){

        PlanAndTaskDTO planAndTaskDTO = new PlanAndTaskDTO();
        PlanEntity plan = planService.getPlanByPlanName(planId);
        planAndTaskDTO.setPlan(plan);

        List<TaskEntity> tasks = taskService.getTasksByPlanId(planId);
        planAndTaskDTO.setTasks(tasks);

        return planAndTaskDTO;
    }

    /**
     * @description: 通过用户id，查询计划
     * @author: hong
     * @date: 2024/7/23 23:57
     **/
    @GetMapping("/getPlansByUserId")
    public List<PlanEntity> getPlansByUserId(@RequestParam String userId){
        return planService.getPlansByUserId(userId);
    }

    /**
     * @description: 通过用户id，查询任务
     * @author: hong
     * @date: 2024/7/24 10:11
     **/
    @GetMapping("/getTasksByUserId")
    public List<TaskEntity> getTasksByUserId(@RequestParam String userId){
        return taskService.getTasksByUserId(userId);
    }

}
