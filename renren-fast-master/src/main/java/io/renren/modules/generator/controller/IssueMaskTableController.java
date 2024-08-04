package io.renren.modules.generator.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.service.IssueMaskTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 16:54:01
 */
@RestController
@RequestMapping("generator/issuemasktable")
public class IssueMaskTableController {
    @Autowired
    private IssueMaskTableService issueMaskTableService;

    /**
     * 列表，接收人可见
     */
    @RequestMapping("/recipientslist")
    @RequiresPermissions("generator:issuemasktable:list")
    public R recipientslist(@RequestParam Map<String, Object> params){
        PageUtils page = issueMaskTableService.recipientsqueryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表，发起人可见
     */
    @RequestMapping("/creatorlist")
    @RequiresPermissions("generator:issuemasktable:list")
    public R creatorlist(@RequestParam Map<String, Object> params){
        PageUtils page = issueMaskTableService.creatorqueryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表，审核人可见
     */
    @RequestMapping("/Auditlist")
    @RequiresPermissions("generator:issuemasktable:list")
    public R Auditlist(@RequestParam Map<String, Object> params){
        PageUtils page = issueMaskTableService.AuditqueryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{issuemaskId}")
    @RequiresPermissions("generator:issuemasktable:info")
    public R info(@PathVariable("issuemaskId") Integer issuemaskId){
		IssueMaskTableEntity issueMaskTable = issueMaskTableService.getById(issuemaskId);

        return R.ok().put("issueMaskTable", issueMaskTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuemasktable:save")
    public R save(@RequestBody IssueMaskTableEntity issueMaskTable){
		issueMaskTableService.save(issueMaskTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuemasktable:update")
    public R update(@RequestBody IssueMaskTableEntity issueMaskTable){
		issueMaskTableService.updateById(issueMaskTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuemasktable:delete")
    public R delete(@RequestBody Integer[] issuemaskIds){
		issueMaskTableService.removeByIds(Arrays.asList(issuemaskIds));

        return R.ok();
    }

}
