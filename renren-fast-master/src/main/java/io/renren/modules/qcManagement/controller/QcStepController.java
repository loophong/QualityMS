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

import io.renren.modules.qcManagement.entity.QcStepEntity;
import io.renren.modules.qcManagement.service.QcStepService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-25 14:45:45
 */
@RestController
@RequestMapping("qcPlan/step")
public class QcStepController {
    @Autowired
    private QcStepService qcStepService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("qcPlan:step:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = qcStepService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{stepId}")
    @RequiresPermissions("qcPlan:step:info")
    public R info(@PathVariable("stepId") Integer stepId){
		QcStepEntity qcStep = qcStepService.getById(stepId);

        return R.ok().put("qcStep", qcStep);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("qcPlan:step:save")
    public R save(@RequestBody QcStepEntity qcStep){
		qcStepService.save(qcStep);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("qcPlan:step:update")
    public R update(@RequestBody QcStepEntity qcStep){
		qcStepService.updateById(qcStep);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("qcPlan:step:delete")
    public R delete(@RequestBody Integer[] stepIds){
		qcStepService.removeByIds(Arrays.asList(stepIds));

        return R.ok();
    }

}
