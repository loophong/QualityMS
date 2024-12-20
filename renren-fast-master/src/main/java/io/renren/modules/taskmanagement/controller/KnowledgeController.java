package io.renren.modules.taskmanagement.controller;

import com.alibaba.excel.EasyExcel;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.taskmanagement.dto.PlanQueryParamDTO;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import io.renren.modules.taskmanagement.service.PlanService;
import io.renren.modules.taskmanagement.vo.PlanExportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
    @PostMapping("/list")
    public R getHistoryPlan(@RequestBody PlanQueryParamDTO planQueryParamDTO) {
        log.info("任务知识库查询：{}", planQueryParamDTO);
        PageUtils page = planService.queryPageGetHistoryPlan(planQueryParamDTO);
        return R.ok().put("page", page);
    }


    /** 
     * @description: 导出知识库
     * @param: response 
     * @return: void 
     * @author: hong
     * @date: 2024/12/19 16:34
     */ 
    @RequestMapping("/export")
    public void export(HttpServletResponse response){
        List<PlanExportVO> planExportVO = planService.exportKnowledgeBase();
        String fileName = "任务表" + System.currentTimeMillis() + ".xlsx";
        try {
            EasyExcel.write(response.getOutputStream(), PlanExportVO.class).sheet("任务表").doWrite(planExportVO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    /** 
     * @description: 从知识库中移除
     * @param: tmPid 
     * @return: io.renren.common.utils.R 
     * @author: hong
     * @date: 2024/12/19 16:33
     */ 
    @GetMapping("/removeFromBase")
    public R removeFromBase(String tmPid) {
        PlanEntity plan = planService.getById(tmPid);
        plan.setAddBase(0);
        planService.updateById(plan);
        return R.ok();
    }

}
