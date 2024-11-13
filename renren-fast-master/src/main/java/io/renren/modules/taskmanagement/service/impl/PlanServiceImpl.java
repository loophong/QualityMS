package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.taskmanagement.dao.TaskDao;
import io.renren.modules.taskmanagement.dto.PlanDTO;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.FileService;
import io.renren.modules.taskmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.PlanDao;
import io.renren.modules.taskmanagement.service.PlanService;

@Slf4j
@Service("planService")
public class PlanServiceImpl extends ServiceImpl<PlanDao, PlanEntity> implements PlanService {

    @Autowired
    private PlanDao planDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FileService fileService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PlanEntity> page = this.page(
                new Query<PlanEntity>().getPage(params),
                new QueryWrapper<PlanEntity>()
        );

        System.out.println(page.getRecords().toString());

        return new PageUtils(page);
    }

    @Override
    public boolean isPlanNumberUsed(String planId) {
        return planDao.countByPlanNumber(planId) > 0;
    }

    @Override
    public PlanEntity getPlanByPlanName(String planId) {
        return planDao.selectOne(new LambdaQueryWrapper<PlanEntity>().eq(PlanEntity::getPlanId, planId));

    }

    @Override
    public List<PlanEntity> getPlansByUserId(String userId) {
        return planDao.getPlansByUserId(userId);
    }

    @Override
    public List<Map<String, Integer>> home() {
        int year = DateUtils.getZeroTime().getYear() + 1900;

        // 查询当年的计划数量,计划开始时间年份模糊匹配 year
        int planNum = planDao.selectList(new QueryWrapper<PlanEntity>().like("plan_start_date", year)).size();
        // 查询当年 已完成 计划数量,计划开始时间年份模糊匹配 year
        int completedPlanNum = planDao.selectList(new QueryWrapper<PlanEntity>().eq("plan_current_state", TaskStatus.COMPLETED).like("plan_start_date", year)).size();

        List<Map<String, Integer>> maps = new ArrayList<>();
        Map<String, Integer> planNumMap = new HashMap<>();
        planNumMap.put("planNum", planNum);
        maps.add(planNumMap);

        Map<String, Integer> completedPlanNumMap = new HashMap<>();
        completedPlanNumMap.put("completedPlanNum", completedPlanNum);
        maps.add(completedPlanNumMap);

        return maps;
    }

    @Override
    public List<PlanStatisticsLabelDto> getPlanLabel() {
        return planDao.getPlanLabel();
    }

    @Override
    public PageUtils queryPageUnfinishedPlan(Map<String, Object> params) {
        IPage<PlanEntity> page = this.page(
                new Query<PlanEntity>().getPage(params),
                new QueryWrapper<PlanEntity>().ne("plan_current_state", TaskStatus.COMPLETED)
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageFinishedPlan(Map<String, Object> params) {
        IPage<PlanEntity> page = this.page(
                new Query<PlanEntity>().getPage(params),
                new QueryWrapper<PlanEntity>().eq("plan_current_state", TaskStatus.COMPLETED)
        );
        return new PageUtils(page);
    }

    /**
     * @description: 获取任务-知识库
     * @param: params
     * @return: io.renren.common.utils.PageUtils
     * @author: hong
     * @date: 2024/11/10 17:19
     */
    @Override
    public PageUtils queryPageGetHistoryPlan(Map<String, Object> params) {
        IPage<PlanEntity> page = this.page(
                new Query<PlanEntity>().getPage(params),
                new QueryWrapper<PlanEntity>().eq("plan_current_state", TaskStatus.COMPLETED)
        );
        return new PageUtils(page);
    }

    /** 
     * @description: 保存计划，直系任务，文件
     * @param: planDTO 
     * @return: void 
     * @author: hong
     * @date: 2024/11/10 17:25
     */ 
    @Override
    public void saveAllPlanInfo(PlanDTO planDTO) {
        // 保存计划信息
        PlanEntity plan = new PlanEntity();
        BeanUtils.copyProperties(planDTO.getPlan(), plan);
        log.info("plan: " + plan);
        planDao.insert(plan);

        // 保存任务信息
        List<TaskEntity> tasks = planDTO.getTasks();
        log.info("tasks: " + tasks);
        taskService.saveBatch(tasks);

        // 保存附件信息
        List<FileEntity> files = planDTO.getFiles();
        log.info("files: " + files);
        fileService.saveBatch(files);
    }

    /**
     * @description: 更新计划，直系任务，文件
     * @param: planDTO
     * @return: void
     * @author: hong
     * @date: 2024/11/10 17:55
     */
    @Override
    public void updateAllPlanInfo(PlanDTO planDTO) {
        // 更新计划信息
        PlanEntity plan = new PlanEntity();
        log.info("plan: " + plan);
        BeanUtils.copyProperties(planDTO.getPlan(), plan);
        planDao.updateById(plan);

        // 更新任务信息
        List<TaskEntity> tasks = planDTO.getTasks();
        log.info("tasks: " + tasks);
        taskService.remove(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, plan.getPlanId()));
        taskService.saveBatch(tasks);

        // 更新附件信息
        List<FileEntity> files = planDTO.getFiles();
        log.info("files: " + files);
        fileService.remove(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, plan.getPlanId()));
        fileService.saveBatch(files);
    }

}