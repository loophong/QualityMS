package io.renren.modules.generator.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.entity.IssueTypeTableEntity;
import io.renren.modules.generator.service.IssueTypeTableService;
import io.renren.modules.generator.service.impl.IssueMaskTableServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 问题类别字典
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 10:59:16
 */
@RestController
@RequestMapping("generator/issuetypetable")
public class IssueTypeTableController {
    @Autowired
    private IssueTypeTableService issueTypeTableService;
    @Autowired
    private IssueMaskTableServiceImpl issueMaskTableService;

    /**
     * 任务派发
     */
    @RequestMapping("/mask")
    @RequiresPermissions("generator:issuemasktable:save")
    public R save(@RequestBody IssueMaskTableEntity issueMaskTable){
        issueMaskTableService.save(issueMaskTable);
        return R.ok();
    }

    /**
     * 获取所有问题列表
     */
    @RequestMapping("/issuestype")
    @RequiresPermissions("generator:issuetypetable:list")
    public R list(){
//        System.out.println("=====获取问题列表----开始");
        List<IssueTypeTableEntity> category = issueTypeTableService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", category);
//        System.out.println(response);
//        System.out.println("=====获取问题列表----结束");
        return R.ok().put("category", category);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuetypetable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issueTypeTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{issueCategoryId}")
    @RequiresPermissions("generator:issuetypetable:info")
    public R info(@PathVariable("issueCategoryId") Integer issueCategoryId){
		IssueTypeTableEntity issueTypeTable = issueTypeTableService.getById(issueCategoryId);

        return R.ok().put("issueTypeTable", issueTypeTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuetypetable:save")
    public R save(@RequestBody IssueTypeTableEntity issueTypeTable){
		issueTypeTableService.save(issueTypeTable);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuetypetable:update")
    public R update(@RequestBody IssueTypeTableEntity issueTypeTable){
		issueTypeTableService.updateById(issueTypeTable);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuetypetable:delete")
    public R delete(@RequestBody Integer[] issueCategoryIds){
		issueTypeTableService.removeByIds(Arrays.asList(issueCategoryIds));

        return R.ok();
    }

}
