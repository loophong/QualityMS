package io.renren.modules.taskmanagement.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.app.service.UserService;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.service.MessageNotificationService;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.taskmanagement.dao.TaskDao;
import io.renren.modules.taskmanagement.dto.PlanDTO;
import io.renren.modules.taskmanagement.dto.PlanQueryParamDTO;
import io.renren.modules.taskmanagement.entity.*;
import io.renren.modules.taskmanagement.service.*;
import io.renren.modules.taskmanagement.vo.PlanExportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.taskmanagement.dao.PlanDao;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("planService")
public class PlanServiceImpl extends ServiceImpl<PlanDao, PlanEntity> implements PlanService {
    @Autowired
    private UserService userService;

    @Autowired
    private PlanDao planDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FileService fileService;
    @Autowired
    private IndicatorDictionaryService indicatorDictionaryService;
    @Autowired
    private PlanApprovalTableService planApprovalTableService;
    @Autowired
    private MessageNotificationService messageService;
    @Autowired
    private ApprovalService approvalService;

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

    @Override
    public PageUtils queryPageFinishedPlan(PlanQueryParamDTO planQueryParamDTO) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", planQueryParamDTO.getPage().toString());
//        params.put("limit", planQueryParamDTO.getLimit().toString());
//        IPage<PlanEntity> page = this.page(
//                new Query<PlanEntity>().getPage(params),
//                new QueryWrapper<PlanEntity>()
//                        .eq("plan_current_state", TaskStatus.COMPLETED)
//                        .eq("plan_id", planQueryParamDTO.getPlan().getPlanId())
//                        .eq("plan_name", planQueryParamDTO.getPlan().getPlanName())
//        );
        PlanEntity plan = new PlanEntity();
        BeanUtils.copyProperties(planQueryParamDTO.getPlan(), plan);
//        plan.setPlanCurrentState(TaskStatus.COMPLETED);
        plan.setPlanIsCompleted(1);
        Page<PlanEntity> page = new Page<>(planQueryParamDTO.getPage(), planQueryParamDTO.getLimit());
        Page<PlanEntity> result = planDao.queryPageByParams(page, plan);
        return new PageUtils(result);
    }

    @Override
    public List<PlanExportVO> export() {
        List<PlanExportVO> planList = planDao.export();
        List<PlanExportVO> planExportVO = new ArrayList<>();
        log.info("planExportVOS" + planList);
        for (PlanExportVO plan : planList) {
            planExportVO.add(plan);
            List<PlanExportVO> taskList = taskDao.selectTaskByPlanId(plan.getPlanId());
            log.info("taskList" + taskList);
            planExportVO.addAll(taskList);
        }

        // 统一将VO中的用户ID转为名称
        for (PlanExportVO plan : planExportVO) {
            convert(plan);
        }

        log.info("转换后的vo：" + planExportVO);
        return planExportVO;
    }

    @Override
    public List<PlanExportVO> exportKnowledgeBase() {
        List<PlanExportVO> planList = planDao.exportKnowledgeBase();
        List<PlanExportVO> planExportVO = new ArrayList<>();
        log.info("planExportVOS" + planList);
        for (PlanExportVO plan : planList) {
            planExportVO.add(plan);
            List<PlanExportVO> taskList = taskDao.selectTaskByPlanId(plan.getPlanId());
            log.info("taskList" + taskList);
            planExportVO.addAll(taskList);
        }

        // 统一将VO中的用户ID转为名称
        for (PlanExportVO plan : planExportVO) {
            convert(plan);
        }

        log.info("转换后的vo：" + planExportVO);
        return planExportVO;
    }

    @Override
    public void saveAllPlanInfoAndApproval(PlanDTO planDTO) {
        // 保存计划信息
        PlanEntity plan = new PlanEntity();
        BeanUtils.copyProperties(planDTO.getPlan(), plan);
        plan.setPlanCurrentState(TaskStatus.PREAPPROVAL_IN_PROGRESS);
        log.info("plan: " + plan);
        planDao.insert(plan);

        // 新建审批
        PlanApprovalTableEntity planApprovalTableEntity = new PlanApprovalTableEntity();
        BeanUtils.copyProperties(plan, planApprovalTableEntity);
        planApprovalTableEntity.setApprovalStatus(ApprovalStatus.PENDING);
        planApprovalTableEntity.setPlanPrincipal(plan.getPlanPrincipal());
        planApprovalTableEntity.setPlanExecutor(plan.getPlanExecutor().toString());
        if (plan.getPlanAssociatedIndicatorsId() != null && !plan.getPlanAssociatedIndicatorsId().equals("")) {
            planApprovalTableEntity.setPlanAssociatedIndicatorsId(Integer.valueOf(plan.getPlanAssociatedIndicatorsId()));
        }
        planApprovalTableEntity.setPlanSubmissionTime(new Date());
        planApprovalTableEntity.setApprover(plan.getPlanAuditor());
        planApprovalTableEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));
        log.info("planApprovalTableEntity: " + planApprovalTableEntity);
        planApprovalTableService.save(planApprovalTableEntity);

        // 保存任务信息
        List<TaskEntity> tasks = planDTO.getTasks();
        log.info("tasks: " + tasks);
        taskService.saveBatch(tasks);

        // 保存附件信息
        List<FileEntity> files = planDTO.getFiles();
        log.info("files: " + files);
        fileService.saveBatch(files);

        // 发送通知
        messageService.sendMessages(new CreateNoticeParams(ShiroUtils.getUserId(), new Long[]{Long.valueOf(plan.getPlanAuditor())}, "您有一个新建计划未审批", "新建计划审批通知", "plan_approval_page"));
    }

    @Transactional
    @Override
    public void reApproval(PlanDTO planDTO) {
        // 更新计划信息
        PlanEntity plan = new PlanEntity();
        log.info("plan: " + plan);
        BeanUtils.copyProperties(planDTO.getPlan(), plan);
        plan.setPlanCurrentState(TaskStatus.PREAPPROVAL_IN_PROGRESS);
        planDao.updateById(plan);

        // 更新任务信息
        List<TaskEntity> tasks = planDTO.getTasks();
        log.info("tasks: " + tasks);
        taskService.remove(new LambdaQueryWrapper<TaskEntity>().eq(TaskEntity::getTaskAssociatedPlanId, plan.getPlanId()));
        for (TaskEntity task : tasks) {
            task.setTaskCurrentState(TaskStatus.PREAPPROVAL_IN_PROGRESS);
        }
        taskService.saveBatch(tasks);

        // 更新附件信息
        List<FileEntity> files = planDTO.getFiles();
        log.info("files: " + files);
        fileService.remove(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, plan.getPlanId()));
        fileService.saveBatch(files);

        // 新建审批
        PlanApprovalTableEntity planApprovalTableEntity = new PlanApprovalTableEntity();
        BeanUtils.copyProperties(plan, planApprovalTableEntity);
        planApprovalTableEntity.setApprovalStatus(ApprovalStatus.PENDING);
        planApprovalTableEntity.setPlanPrincipal(plan.getPlanPrincipal());
        planApprovalTableEntity.setPlanExecutor(plan.getPlanExecutor().toString());
        if (plan.getPlanAssociatedIndicatorsId() != null && !plan.getPlanAssociatedIndicatorsId().equals("")) {
            planApprovalTableEntity.setPlanAssociatedIndicatorsId(Integer.valueOf(plan.getPlanAssociatedIndicatorsId()));
        }
        planApprovalTableEntity.setPlanSubmissionTime(new Date());
        planApprovalTableEntity.setApprover(plan.getPlanAuditor());
        planApprovalTableEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));
        log.info("planApprovalTableEntity: " + planApprovalTableEntity);
        planApprovalTableService.save(planApprovalTableEntity);

        // 发送通知
        messageService.sendMessages(
                new CreateNoticeParams(
                        ShiroUtils.getUserId(),
                        new Long[]{Long.valueOf(plan.getPlanAuditor())},
                        "您有一个新建计划未审批",
                        "新建计划审批通知",
                        "plan_approval_page"));
    }

    @Override
    public PageUtils queryPageByPlanParams(PlanQueryParamDTO planQueryParamDTO) {


        PlanEntity plan = planQueryParamDTO.getPlan();
        Page<PlanEntity> page = new Page<>(planQueryParamDTO.getPage(), planQueryParamDTO.getLimit());

        IPage<PlanEntity> planList = planDao.selectPage(
                page,
                new LambdaQueryWrapper<PlanEntity>()
                        .eq(plan.getPlanId() != null, PlanEntity::getPlanId, plan.getPlanId())
                        .eq(plan.getPlanName() != null, PlanEntity::getPlanName, plan.getPlanName())
                        .eq(plan.getPlanAssociatedIndicatorsId() != null, PlanEntity::getPlanAssociatedIndicatorsId, plan.getPlanAssociatedIndicatorsId())
                        .orderByDesc(PlanEntity::getPlanStartDate)
        );

        return new PageUtils(planList);
    }

    @Override
    public List<PlanExportVO> exportBase() {
        List<PlanExportVO> planList = planDao.exportBase();
        List<PlanExportVO> planExportVO = new ArrayList<>();
        log.info("planExportVOS" + planList);
        for (PlanExportVO plan : planList) {
            planExportVO.add(plan);
            List<PlanExportVO> taskList = taskDao.selectTaskByPlanId(plan.getPlanId());
            log.info("taskList" + taskList);
            planExportVO.addAll(taskList);
        }

        // 统一将VO中的用户ID转为名称
        for (PlanExportVO plan : planExportVO) {
            convert(plan);
        }

        log.info("转换后的vo：" + planExportVO);
        return planExportVO;
    }

    private void convert(PlanExportVO plan) {

        if (plan.getPrincipal() != null) {
            String username = userService.getUsernameById(plan.getPrincipal());
            if (username != null) {
                plan.setPrincipal(username);
            }
        }

        if (plan.getAuditor() != null) {
            String username = userService.getUsernameById(plan.getAuditor());
            if (username != null) {
                plan.setAuditor(username);
            }
        }

        if (plan.getExecutor() != null) {
            String input = plan.getExecutor();
            // 使用replaceAll 去掉[]
            input = input.replaceAll("\\[|\\]", "");
            // 使用replaceAll 去掉"
            input = input.replaceAll("\"", "");
            String[] output = input.split(","); // 按逗号分割
            log.info("output: " + Arrays.toString(output));

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < output.length; i++) {
                String username = userService.getUsernameById(output[i]);
                if (username != null) {
                    result.append(username);
                }
                if (i < output.length - 1) {
                    result.append(",");
                }
            }
            log.info("result: " + result);
            plan.setExecutor(result.toString());
        }

        if (plan.getAssociatedIndicatorsId() != null && !"".equals(plan.getAssociatedIndicatorsId())) {
            log.info("indicatorId: " + plan.getAssociatedIndicatorsId());
            String indicatorName = indicatorDictionaryService.getById(plan.getAssociatedIndicatorsId()).getIndicatorName();
            plan.setAssociatedIndicatorsId(indicatorName);
            log.info("indicatorName: " + indicatorName);
        }

        if (plan.getCurrentState() != null) {
            switch (plan.getCurrentState()) {
                case "COMPLETED":
                    plan.setCurrentState("已完成");
                    break;
                case "OVERDUE":
                    plan.setCurrentState("超期");
                    break;
                case "IN_PROGRESS":
                    plan.setCurrentState("进行中");
                    break;
                case "NOT_STARTED":
                    plan.setCurrentState("未开始");
                    break;
                case "APPROVAL_IN_PROGRESS":
                    plan.setCurrentState("审核中");
                    break;
            }
        }


    }


    /**
     * @description: 获取任务-知识库
     * @param: params
     * @return: io.renren.common.utils.PageUtils
     * @author: hong
     * @date: 2024/11/10 17:19
     */
    @Override
    public PageUtils queryPageGetHistoryPlan(PlanQueryParamDTO planQueryParamDTO) {
        PlanEntity plan = planQueryParamDTO.getPlan();
        Page<PlanEntity> page = new Page<>(planQueryParamDTO.getPage(), planQueryParamDTO.getLimit());

        IPage<PlanEntity> planList = planDao.selectPage(
                page,
                new LambdaQueryWrapper<PlanEntity>()
                        .like(plan.getPlanId() != null && !"".equals(plan.getPlanId()), PlanEntity::getPlanId, plan.getPlanId())
                        .like(plan.getPlanName() != null && !"".equals(plan.getPlanName()), PlanEntity::getPlanName, plan.getPlanName())
                        .eq(plan.getPlanAssociatedIndicatorsId() != null && !"".equals(plan.getPlanAssociatedIndicatorsId()), PlanEntity::getPlanAssociatedIndicatorsId, plan.getPlanAssociatedIndicatorsId())
                        .eq(PlanEntity::getAddBase, 1)
                        .orderByDesc(PlanEntity::getPlanStartDate)
        );

        return new PageUtils(planList);
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
        int i = planDao.updateById(plan);
        log.info("更新计划信息" + i);
        if (i == 1) {
            // 将已有审批改为取消
            planApprovalTableService.update(new LambdaUpdateWrapper<PlanApprovalTableEntity>()
                    .eq(PlanApprovalTableEntity::getPlanId, plan.getPlanId())
                    .eq(PlanApprovalTableEntity::getApprovalStatus, ApprovalStatus.PENDING)
                    .set(PlanApprovalTableEntity::getApprovalStatus, ApprovalStatus.CANCEL));
            // 计划信息发生改变，修改状态，发起审批
            plan.setPlanCurrentState(TaskStatus.UPDATE_APPROVAL_IN_PROGRESS);
            // 新建审批
            PlanApprovalTableEntity planApprovalTableEntity = new PlanApprovalTableEntity();
            BeanUtils.copyProperties(plan, planApprovalTableEntity);
            planApprovalTableEntity.setApprovalStatus(ApprovalStatus.PENDING);
            planApprovalTableEntity.setPlanPrincipal(plan.getPlanPrincipal());
            planApprovalTableEntity.setPlanExecutor(plan.getPlanExecutor().toString());
            if (plan.getPlanAssociatedIndicatorsId() != null && !plan.getPlanAssociatedIndicatorsId().equals("")) {
                planApprovalTableEntity.setPlanAssociatedIndicatorsId(Integer.valueOf(plan.getPlanAssociatedIndicatorsId()));
            }
            planApprovalTableEntity.setPlanSubmissionTime(new Date());
            planApprovalTableEntity.setApprover(plan.getPlanAuditor());
            planApprovalTableEntity.setSubmitter(String.valueOf(ShiroUtils.getUserId()));
            log.info("planApprovalTableEntity: " + planApprovalTableEntity);
            planApprovalTableService.save(planApprovalTableEntity);
            planDao.updateById(plan);
        }

        // 更新任务信息
        ArrayList<TaskEntity> newTaskList = new ArrayList<>();
        List<TaskEntity> tasks = planDTO.getTasks();
        log.info("tasks: " + tasks);
        for (TaskEntity task : tasks) {
            TaskEntity taskEntity = taskDao.selectById(task.getTmTid());
            log.info("当前task为" + task.getTaskId());
            log.info("当前task是否存在" + taskEntity);
            if (taskEntity != null) {

                if (!task.getTaskName().equals(taskEntity.getTaskName()) || !task.getTaskContent().equals(taskEntity.getTaskContent())
                        || !task.getTaskStartDate().equals(taskEntity.getTaskStartDate()) || !task.getTaskScheduleCompletionDate().equals(taskEntity.getTaskScheduleCompletionDate())
                        || !task.getTaskPrincipal().equals(taskEntity.getTaskPrincipal()) || !task.getTaskAuditor().equals(taskEntity.getTaskAuditor())
                        || !task.getTaskExecutor().equals(taskEntity.getTaskExecutor())) {

                    log.info("任务存在，发生改变，更新任务");
                    task.setTaskCurrentState(TaskStatus.UPDATE_APPROVAL_IN_PROGRESS);
                    // 检查修改前任务是否有正在审批的记录，如果有则改为取消
                    approvalService.update(new LambdaUpdateWrapper<ApprovalEntity>()
                            .eq(ApprovalEntity::getTaskId, taskEntity.getTaskId())
                            .eq(ApprovalEntity::getApprovalStatus, ApprovalStatus.PENDING)
                            .set(ApprovalEntity::getApprovalStatus, ApprovalStatus.CANCEL));


                    approvalService.createTaskApproval(task, "UPDATE");

                } else {
                    log.info("任务存在，未发生改变，不更新任务");
                }
                // 更新
                taskDao.updateById(task);
                TaskEntity newTask = taskDao.selectById(task.getTmTid());
                newTaskList.add(newTask);
            } else {
                // 插入
                log.info("当前任务不存在，新增任务");
                task.setTaskCurrentState(TaskStatus.PREAPPROVAL_IN_PROGRESS);
                // 新建任务前检查是否有正在审批的记录，如果有则改为取消
                approvalService.update(new LambdaUpdateWrapper<ApprovalEntity>()
                        .eq(ApprovalEntity::getTaskId, taskEntity.getTaskId())
                        .eq(ApprovalEntity::getApprovalStatus, ApprovalStatus.PENDING)
                        .set(ApprovalEntity::getApprovalStatus, ApprovalStatus.CANCEL));
                approvalService.createTaskApproval(task, "NEW");

                newTaskList.add(task);
            }
            log.info("父节点" + task.getTaskParentNode());
        }


        taskService.remove(new LambdaQueryWrapper<TaskEntity>()
                .eq(TaskEntity::getTaskAssociatedPlanId, plan.getPlanId())
                .eq(TaskEntity::getTaskParentNode, plan.getPlanId()));
        taskService.saveBatch(newTaskList);

        // 更新附件信息
        List<FileEntity> files = planDTO.getFiles();
        log.info("files: " + files);
        fileService.remove(new LambdaQueryWrapper<FileEntity>().eq(FileEntity::getPlanId, plan.getPlanId()));
        fileService.saveBatch(files);
    }

    @Override
    public PageUtils queryPageByParams(PlanQueryParamDTO planQueryParamDTO) {
        PlanEntity plan = new PlanEntity();
        BeanUtils.copyProperties(planQueryParamDTO.getPlan(), plan);
        plan.setPlanIsCompleted(0);
        log.info("查询前的plan: " + plan);
        Page<PlanEntity> page = new Page<>(planQueryParamDTO.getPage(), planQueryParamDTO.getLimit());
        Page<PlanEntity> result = planDao.queryPageByParams(page, plan);
        // 封装分页结果
        return new PageUtils(result);

    }


}