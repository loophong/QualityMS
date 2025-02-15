package io.renren.modules.taskmanagement.controller;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.log.Log;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.app.service.UserService;
import io.renren.modules.taskmanagement.dto.PlanDTO;
import io.renren.modules.taskmanagement.dto.PlanQueryParamDTO;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.*;
import io.renren.modules.taskmanagement.vo.PlanExportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * 任务管理模块计划表
 *
 * @author hong
 * @email ${email}
 * @date 2024-07-22 10:16:37
 */
@Slf4j
@RestController
@RequestMapping("taskmanagement/plan")
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PlanApprovalTableService planApprovalTableService;
    @Autowired
    private UserService userService;
    /**
     * @description: 全部计划导出
     * @param: response
     * @return: void
     * @author: hong
     * @date: 2024/12/22 10:29
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response) {
        List<PlanExportVO> planExportVO = planService.export();
        String fileName = "任务表" + System.currentTimeMillis() + ".xlsx";
        try {
            EasyExcel.write(response.getOutputStream(), PlanExportVO.class).sheet("任务表").doWrite(planExportVO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @description: 知识库导出
     * @param: response
     * @return: void
     * @author: hong
     * @date: 2024/12/22 10:29
     */
    @RequestMapping("/exportBase")
    public void exportBase(HttpServletResponse response) {
        List<PlanExportVO> planExportVO = planService.exportBase();
        String fileName = "任务表" + System.currentTimeMillis() + ".xlsx";
        try {
            EasyExcel.write(response.getOutputStream(), PlanExportVO.class).sheet("任务表").doWrite(planExportVO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * @description: 计划入库
     * @param: tmPid
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/12/19 15:10
     */
    @RequestMapping("/addBase/{tmPid}")
    public R addBase(@PathVariable("tmPid") Long tmPid) {
        log.info("计划入库：" + tmPid);
        PlanEntity plan = planService.getById(tmPid);
        plan.setAddBase(1);
        planService.updateById(plan);
        return R.ok();
    }

    /**
     * @description: 分页查询
     * @param: planQueryParamDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/12/16 16:58
     */
    @PostMapping("/queryPageByParams")
    public R queryPageByParams(@RequestBody PlanQueryParamDTO planQueryParamDTO) {
        log.info("查询条件：" + planQueryParamDTO);
        PageUtils page = planService.queryPageByPlanParams(planQueryParamDTO);
        return R.ok().put("page", page);
    }

    /**
     * @description: 图中点击计划时，查询计划的全部信息和计划文件
     * @param: null
     * @return:
     * @author: hong
     * @date: 2024/12/12 15:50
     */
    @PostMapping("/planAllInfo")
    public R planAllInfo(@RequestBody String param) throws Exception {
        log.info("param:" + param);
        Map<String, Object> jsonMap = objectMapper.readValue(param, Map.class);
        String planId = (String) jsonMap.get("paramPlanId");
        log.info("paramPlanId:" + planId);
        PlanEntity plan = planService.query().eq("plan_id", planId).one();
        // 查询plan的文件
        List<FileEntity> files = fileService.list(new QueryWrapper<FileEntity>().eq("plan_id", planId));
        if (files != null) {
            return R.ok().put("plan", plan).put("files", files);
        } else {
            return R.ok().put("plan", plan).put("files", null);
        }
    }

    /**
     * @description: PlanStatisticsLabelDto
     * @author: hong
     * @date: 2024/9/25 15:35
     * @version: 1.0
     */
    @RequestMapping("/getPlanLabel")
    public List<PlanStatisticsLabelDto> getPlanLabel() {

        // 查询计划名称和计划id
        List<PlanStatisticsLabelDto> plans = planService.getPlanLabel();

        log.info("计划名称和计划id" + plans.toString());

        return plans;
    }

    /**
     * @description: 大屏展示计划和任务信息
     * @author: hong
     * @date: 2024/9/25 9:55
     * @version: 1.0
     */
    @RequestMapping("/home")
    public List<Map<String, Integer>> home() {
        // 查询当年的计划数量
        List<Map<String, Integer>> plan = planService.home();

        List<Map<String, Integer>> task = taskService.home();

        plan.add(0, task.get(0));
        plan.add(1, task.get(1));

        log.info("大屏数据" + plan.toString());

        return plan;
    }

    /**
     * @description: 大屏展示-查询计划总数
     * @author: hong
     * @date: 2024/10/24 15:32
     * @version: 1.0
     */
    @RequestMapping("/getPlanCount")
    public R getPlanCount() {
        int count = planService.query().count();
        log.info("计划总数：" + count);
        return R.ok().put("count", count);
    }

    /**
     * @description: 查询未完成计划
     * @author: hong
     * @date: 2024/10/15 15:11
     * @version: 1.0
     */
    @RequestMapping("/unfinished")
    @RequiresPermissions("taskmanagement:plan:list")
    public R unfinishedList(@RequestParam Map<String, Object> params) {
        PageUtils page = planService.queryPageUnfinishedPlan(params);
        System.out.println(page.getList().toString());
        return R.ok().put("page", page);
    }

    /**
     * @description: 分页查询，查询未完成的任务
     * @param: planQueryParamDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/13 15:47
     */
    @RequestMapping("/queryUnfinished")
    @RequiresPermissions("taskmanagement:plan:list")
    public R queryUnfinished(@RequestBody PlanQueryParamDTO planQueryParamDTO) {
        log.info("计划查询条件：{}", planQueryParamDTO);
        PageUtils page = planService.queryPageByParams(planQueryParamDTO);
        return R.ok().put("page", page);
    }

    /**
     * @description: 分页查询已完成的任务
     * @param: planQueryParamDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/13 15:48
     */
    @RequestMapping("/finished")
    @RequiresPermissions("taskmanagement:plan:list")
    public R finishedList(@RequestBody PlanQueryParamDTO planQueryParamDTO) {
        log.info("计划查询条件：" + planQueryParamDTO);
        PageUtils page = planService.queryPageFinishedPlan(planQueryParamDTO);
        return R.ok().put("page", page);
    }

    @RequestMapping("/finish")
    @RequiresPermissions("taskmanagement:plan:list")
    public R finishedList(@RequestParam Map<String, Object> params) {
        PageUtils page = planService.queryPageFinishedPlan(params);
        System.out.println(page.getList().toString());
        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("taskmanagement:plan:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = planService.queryPage(params);

        System.out.println(page.getList().toString());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tmPid}")
    @RequiresPermissions("taskmanagement:plan:info")
    public R info(@PathVariable("tmPid") Long tmPid) {
        PlanEntity taskManagementPlan = planService.getById(tmPid);

        return R.ok().put("taskManagementPlan", taskManagementPlan);
    }

    /**
     * 保存- 已被下面方法重写
     */
//    @PostMapping("/save")
//    @RequiresPermissions("taskmanagement:plan:save")
//    public R save(@RequestBody PlanAndTaskDTO planAndTaskDTO) {
//
//        log.info("新增计划：" + planAndTaskDTO);
//
//        planService.save(planAndTaskDTO.getPlan());
//
//        for (int i = 0; i < planAndTaskDTO.getTasks().size(); i++) {
//            taskService.save(planAndTaskDTO.getTasks().get(i));
//        }
//
//        return R.ok();
//    }

    /**
     * @description: 保存计划，直系任务，文件
     * @param: planDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/11/10 17:20
     */
    @Transactional
    @PostMapping("/save")
    @RequiresPermissions("taskmanagement:plan:save")
    public R save(@RequestBody PlanDTO planDTO) {
        log.info("新增计划：" + planDTO);
        planService.saveAllPlanInfo(planDTO);
        return R.ok();
    }

    /**
     * @description: 保存计划，直系任务，文件，并且根据计划的审批人进行送审
     * @param: planDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/12/15 13:18
     */
    @Transactional
    @PostMapping("/saveAndApproval")
    @RequiresPermissions("taskmanagement:plan:save")
    public R saveAndApproval(@RequestBody PlanDTO planDTO) {
        log.info("新增计划：" + planDTO);
        planService.saveAllPlanInfoAndApproval(planDTO);
        return R.ok();
    }

    /**
     * @description: 重新提交审批
     * @param: planDTO
     * @return: io.renren.common.utils.R
     * @author: hong
     * @date: 2024/12/20 13:13
     */
    @Transactional
    @RequestMapping("/reApproval")
    @RequiresPermissions("taskmanagement:plan:update")
    public R reApproval(@RequestBody PlanDTO planDTO) {
        planService.reApproval(planDTO);
        return R.ok();
    }

    /**
     * @description: 更新计划和任务
     * @author: hong
     * @date: 2024/8/30 16:15
     * @version: 1.0
     */
//    @RequestMapping("/update")
//    @RequiresPermissions("taskmanagement:plan:update")
//    public R update(@RequestBody PlanAndTaskDTO planAndTaskDTO) {
////        planService.updateById(taskManagementPlan);
//
//        PlanEntity plan = planAndTaskDTO.getPlan();
//        planService.updateById(plan);
//
//        List<TaskEntity> tasks = planAndTaskDTO.getTasks();
//        tasks.forEach(task -> {
//            log.info("当前task为" + task);
//            if (taskService.isTaskIdUsed(task.getTaskId())) {
//                // 如果任务编号已经被使用，则通过taskid更新任务
//                taskService.updateById(task);
//
//
//            } else {
//                taskService.save(task);
//            }
//        });
//
//        return R.ok();
//    }
    @Transactional
    @RequestMapping("/update")
    @RequiresPermissions("taskmanagement:plan:update")
    public R update(@RequestBody PlanDTO planDTO) {
        planService.updateAllPlanInfo(planDTO);
        return R.ok();
    }


    /**
     * @description: 删除计划时删除其全部子任务
     * @author: hong
     * @date: 2024/8/30 15:51
     * @version: 1.0
     */
    @Transactional
    @RequestMapping("/delete/{planId}")
    @RequiresPermissions("taskmanagement:plan:delete")
    public R delete(@PathVariable String planId) {
        log.info("删除计划的全部信息：{}", planId);
        QueryWrapper<PlanEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        planService.remove(queryWrapper);

        // 当任务关联计划编号=planId的任务全部删除
        QueryWrapper<TaskEntity> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("task_associated_plan_id", planId);
        taskService.remove(taskQueryWrapper);

        // 删除所有的文件
        fileService.remove(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, planId));

        // 删除所有的审批信息
        approvalService.remove(new LambdaQueryWrapper<ApprovalEntity>().eq(ApprovalEntity::getTaskAssociatedPlanId, planId));

        planApprovalTableService.remove(new LambdaQueryWrapper<PlanApprovalTableEntity>().eq(PlanApprovalTableEntity::getPlanId, planId));
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
//    @GetMapping("/getPlanAllInfo")
//    public PlanAndTaskDTO getPlanAllInfo(@RequestParam String planId) {
//
//        PlanAndTaskDTO planAndTaskDTO = new PlanAndTaskDTO();
//        PlanEntity plan = planService.getPlanByPlanName(planId);
//        planAndTaskDTO.setPlan(plan);
//
//        List<TaskEntity> tasks = taskService.getTasksByPlanId(planId);
//        planAndTaskDTO.setTasks(tasks);
//
//        return planAndTaskDTO;
//    }

    /**
     * @description: 查询计划的全部信息、任务、文件
     * @param: planId
     * @return:
     * @author: hong
     * @date: 2024/11/10 17:25
     */
    @GetMapping("/getPlanAllInfo")
    public PlanDTO getPlanAllInfo(@RequestParam String planId) {
        log.info("planId:{}", planId);

        PlanDTO planDTO = new PlanDTO();

        PlanEntity plan = planService.getOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));
        List<TaskEntity> taskList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId));
        List<FileEntity> fileList = fileService.list(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, planId));

        planDTO.setPlan(plan);
        planDTO.setTasks(taskList);
        planDTO.setFiles(fileList);

        return planDTO;
    }


    /**
     * @description: 树状图展示计划的结构
     * @param: planId
     * @return: io.renren.modules.taskmanagement.dto.PlanDTO
     * @author: hong
     * @date: 2024/12/25 9:56
     */
    @GetMapping("/planTreeDisplay")
    public PlanDTO planTreeDisplay(@RequestParam String planId) {
        log.info("planId:{}", planId);

        PlanDTO planDTO = new PlanDTO();

        PlanEntity plan = planService.getOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));
        List<TaskEntity> taskList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, planId));
        List<FileEntity> fileList = fileService.list(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, planId));

        planDTO.setPlan(plan);
        planDTO.setTasks(taskList);
        planDTO.setFiles(fileList);

        return planDTO;
    }




    /**
     * @description: 查询计划的全部信息、直属任务、文件
     * @param: planId
     * @return:
     * @author: hong
     * @date: 2024/11/10 17:25
     */
    @GetMapping("/getPlanAndChildTask")
    public PlanDTO getPlanAndChildTask(@RequestParam String planId) {
        log.info("planId:{}", planId);

        PlanDTO planDTO = new PlanDTO();

        PlanEntity plan = planService.getOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));
        List<TaskEntity> taskList = taskService.list(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskParentNode, planId));
        List<FileEntity> fileList = fileService.list(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, planId));

        planDTO.setPlan(plan);
        planDTO.setTasks(taskList);
        planDTO.setFiles(fileList);

        return planDTO;
    }


    /**
     * @description: 通过用户id，查询计划
     * @author: hong
     * @date: 2024/7/23 23:57
     **/
    @GetMapping("/getPlansByUserId")
    public List<PlanEntity> getPlansByUserId(@RequestParam String userId) {
        return planService.getPlansByUserId(userId);
    }

    /**
     * @description: 通过用户id，查询任务
     * @author: hong
     * @date: 2024/7/24 10:11
     **/
    @GetMapping("/getTasksByUserId")
    public List<TaskEntity> getTasksByUserId(@RequestParam Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    /**
     * @description: 获取当前计划的全部附件
     * @param: planId
     * @return: java.util.List<io.renren.modules.taskmanagement.entity.FileEntity>
     * @author: hong
     * @date: 2024/11/12 10:50
     */
    @GetMapping("/files/{planId}")
    public R getFiles(@PathVariable String planId) {
        List<FileEntity> list = fileService.list(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, planId));
        return R.ok().put("files", list);
    }

}
