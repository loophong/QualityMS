package io.renren.modules.qcManagement.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.qcManagement.entity.QcDictWorkflowEntity;
import io.renren.modules.qcManagement.service.QcDictWorkflowService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 09:22:11
 */
@RestController
@RequestMapping("qcManagement/dictWorkFlow")
public class QcDictWorkflowController {
    @Autowired
    private QcDictWorkflowService qcDictWorkflowService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcManagement:dictWorkFlow:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcDictWorkflowService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dictId}")
    @RequiresPermissions("qcManagement:dictWorkFlow:info")
    public R info(@PathVariable("dictId") Long dictId){
		QcDictWorkflowEntity qcDictWorkflow = qcDictWorkflowService.getById(dictId);

        return R.ok().put("qcDictWorkflow", qcDictWorkflow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcManagement:dictWorkFlow:save")
    public R save(@RequestBody QcDictWorkflowEntity qcDictWorkflow){
		qcDictWorkflowService.save(qcDictWorkflow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcManagement:dictWorkFlow:update")
    public R update(@RequestBody QcDictWorkflowEntity qcDictWorkflow){
		qcDictWorkflowService.updateById(qcDictWorkflow);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcManagement:dictWorkFlow:delete")
    public R delete(@RequestBody Long[] dictIds){
		qcDictWorkflowService.removeByIds(Arrays.asList(dictIds));

        return R.ok();
    }

}
