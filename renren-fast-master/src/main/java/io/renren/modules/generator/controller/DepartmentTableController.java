package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.HashMap;
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

import io.renren.modules.generator.entity.DepartmentTableEntity;
import io.renren.modules.generator.service.DepartmentTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-11 15:25:55
 */
@RestController
@RequestMapping("generator/departmenttable")
public class DepartmentTableController {
    @Autowired
    private DepartmentTableService departmentTableService;
    /**
     * 获取所有部门列表
     */
    @RequestMapping("/fetchDepartments")
    @RequiresPermissions("generator:issuetypetable:list")
    public R list(){
//        System.out.println("=====获取问题列表----开始");
        List<DepartmentTableEntity> departmentTableEntities = departmentTableService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", departmentTableEntities);
//        System.out.println(response);
//        System.out.println("=====获取问题列表----结束");
        return R.ok().put("departmentTableEntities", departmentTableEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:departmenttable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = departmentTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{departmentId}")
    @RequiresPermissions("generator:departmenttable:info")
    public R info(@PathVariable("departmentId") Integer departmentId){
		DepartmentTableEntity departmentTable = departmentTableService.getById(departmentId);

        return R.ok().put("departmentTable", departmentTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:departmenttable:save")
    public R save(@RequestBody DepartmentTableEntity departmentTable){
		departmentTableService.save(departmentTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:departmenttable:update")
    public R update(@RequestBody DepartmentTableEntity departmentTable){
		departmentTableService.updateById(departmentTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:departmenttable:delete")
    public R delete(@RequestBody Integer[] departmentIds){
		departmentTableService.removeByIds(Arrays.asList(departmentIds));

        return R.ok();
    }

}
