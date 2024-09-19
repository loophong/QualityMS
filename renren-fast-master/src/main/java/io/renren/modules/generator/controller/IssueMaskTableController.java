package io.renren.modules.generator.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueMaskTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
     * 获取所有任务列表
     */
    @RequestMapping("/issuemasklist")
    @RequiresPermissions("generator:issuemasktable:list")
    public R list(@RequestParam String issueNumber){
        System.out.println("=====获取任务列表----开始"+issueNumber+"****");
        List<IssueMaskTableEntity> issues = issueMaskTableService.listAll(issueNumber);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", issues);
        System.out.println(response);
        System.out.println("=====获取任务列表----结束");
        return R.ok().put("issues", issues);
    }
    /**
     * 问题对应任务是否全部完成
     */
    @RequestMapping("/records")
    @RequiresPermissions("generator:issuemasktable:list")
    public R handleRectificationRecords(@RequestParam String issueNumber){
//        System.out.println("获取任务编号成功："+issueNumber);
        String fin = issueMaskTableService.listRecords(issueNumber);
        System.out.println("返回的数据为："+fin);
        return R.ok(fin);
    }

    /**
     * 分发问题
     */
    @RequestMapping("/dispach")
    @RequiresPermissions("generator:issuemasktable:list")
    public R dispatch(@RequestParam String issueNumber ,@RequestParam String serialNumber){
        System.out.println("获取数据成功："+issueNumber+"以及"+serialNumber);
        String fin = issueMaskTableService.dispatch(issueNumber,serialNumber);
//        System.out.println("返回的数据为："+fin);
        return R.ok(fin);
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
        System.out.println("+++++++++++++++++++后端获取信息开始：+++++++");
        issueMaskTableService.save(issueMaskTable);
        System.out.println("+++++++++++++++++后端获取信息："+ issueMaskTable+"+++++++");
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuemasktable:update")
    public R update(@RequestBody IssueMaskTableEntity issueMaskTable){
        System.out.println("开始修改整改数据"+issueMaskTable);
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
    /**
     * 审核
     */
    @RequestMapping("/audit")
    @RequiresPermissions("generator:issuemasktable:delete")
    public R audit(@RequestBody Integer[] issuemaskIds){
        for (Integer id : issuemaskIds) {
            // 根据id获取对应的实体对象
            IssueMaskTableEntity issueMaskTable = issueMaskTableService.getById(id);

            if (issueMaskTable != null) {
                // 修改state为“执行中”
                issueMaskTable.setState("执行中");

                // 更新数据库中的记录
                issueMaskTableService.updateById(issueMaskTable);
            } else {
                System.out.println("未找到id为" + id + "的记录");
            }
        }
        return R.ok();
    }

}
