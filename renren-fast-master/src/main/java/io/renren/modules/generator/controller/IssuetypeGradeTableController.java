package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.modules.generator.entity.IssueTypeTableEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.IssuetypeGradeTableEntity;
import io.renren.modules.generator.service.IssuetypeGradeTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-25 14:50:00
 */
@RestController
@RequestMapping("generator/issuetypegradetable")
public class IssuetypeGradeTableController {
    @Autowired
    private IssuetypeGradeTableService issuetypeGradeTableService;
    /**
     * 获取所有问题等级
     */
    @RequestMapping("/issuesgrade")
    @RequiresPermissions("generator:issuetypegradetable:list")
    public R list(){
//        System.out.println("=====获取问题列表----开始");
        List<IssuetypeGradeTableEntity> category = issuetypeGradeTableService.listAll();
        return R.ok().put("category", category);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuetypegradetable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issuetypeGradeTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{gradeId}")
    @RequiresPermissions("generator:issuetypegradetable:info")
    public R info(@PathVariable("gradeId") Integer gradeId){
		IssuetypeGradeTableEntity issuetypeGradeTable = issuetypeGradeTableService.getById(gradeId);

        return R.ok().put("issuetypeGradeTable", issuetypeGradeTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuetypegradetable:save")
    public R save(@RequestBody IssuetypeGradeTableEntity issuetypeGradeTable){
		issuetypeGradeTableService.save(issuetypeGradeTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuetypegradetable:update")
    public R update(@RequestBody IssuetypeGradeTableEntity issuetypeGradeTable){
		issuetypeGradeTableService.updateById(issuetypeGradeTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuetypegradetable:delete")
    public R delete(@RequestBody Integer[] gradeIds){
		issuetypeGradeTableService.removeByIds(Arrays.asList(gradeIds));

        return R.ok();
    }

}
