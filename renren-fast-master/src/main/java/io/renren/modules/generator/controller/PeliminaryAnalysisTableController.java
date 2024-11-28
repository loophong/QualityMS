package io.renren.modules.generator.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.modules.generator.entity.DepartmentTableEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.generator.entity.PeliminaryAnalysisTableEntity;
import io.renren.modules.generator.service.PeliminaryAnalysisTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-20 15:20:17
 */
@RestController
@RequestMapping("generator/peliminaryanalysistable")
public class PeliminaryAnalysisTableController {
    @Autowired
    private PeliminaryAnalysisTableService peliminaryAnalysisTableService;
    /**
     * Excel上传
     */
    @PostMapping("/uploadExcel")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadExcel(@RequestParam("file") MultipartFile file) {
            // 上传文件
            System.out.println("接收到的文件名: " + file.getOriginalFilename());
            return peliminaryAnalysisTableService.uploadExcelFile(file);

    }
    /**
     * 获取所有初步分析列表
     */
    @RequestMapping("/fetchAnalysis")
    @RequiresPermissions("generator:issuetypetable:list")
    public R list(){
//        System.out.println("=====获取问题列表----开始");
        List<PeliminaryAnalysisTableEntity> peliminaryAnalysisTableEntities = peliminaryAnalysisTableService.listAll();
        Map<String, Object> response = new HashMap<>();
//        response.put("code", 0);
//        response.put("issueTable", departmentTableEntities);
//        System.out.println(response);
//        System.out.println("=====获取问题列表----结束");
        return R.ok().put("peliminaryAnalysisTableEntities", peliminaryAnalysisTableEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:peliminaryanalysistable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = peliminaryAnalysisTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{analysisId}")
    @RequiresPermissions("generator:peliminaryanalysistable:info")
    public R info(@PathVariable("analysisId") Integer analysisId){
		PeliminaryAnalysisTableEntity peliminaryAnalysisTable = peliminaryAnalysisTableService.getById(analysisId);

        return R.ok().put("peliminaryAnalysisTable", peliminaryAnalysisTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:peliminaryanalysistable:save")
    public R save(@RequestBody PeliminaryAnalysisTableEntity peliminaryAnalysisTable){
		peliminaryAnalysisTableService.save(peliminaryAnalysisTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:peliminaryanalysistable:update")
    public R update(@RequestBody PeliminaryAnalysisTableEntity peliminaryAnalysisTable){
		peliminaryAnalysisTableService.updateById(peliminaryAnalysisTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:peliminaryanalysistable:delete")
    public R delete(@RequestBody Integer[] analysisIds){
		peliminaryAnalysisTableService.removeByIds(Arrays.asList(analysisIds));

        return R.ok();
    }

}
