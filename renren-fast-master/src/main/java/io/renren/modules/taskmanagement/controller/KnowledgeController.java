package io.renren.modules.taskmanagement.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("taskmanagement/knowledge")
public class KnowledgeController {
    @Autowired
    private PlanService planService;

    /** 
     * @description: 任务知识库查询
     * @param: params 
     * @return: io.renren.common.utils.R 
     * @author: hong
     * @date: 2024/11/10 15:43
     */ 
    @GetMapping
    public R getHistoryPlan(@RequestParam Map<String, Object> params) {
        log.info("任务知识库查询：{}", params);
        PageUtils page = planService.queryPageGetHistoryPlan(params);
        return R.ok().put("page", page);
    }

}
