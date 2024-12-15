package io.renren.modules.spc.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
import io.renren.modules.spc.service.SpcXrchartStandardsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-14 20:55:08
 */
@RestController
@RequestMapping("spc/spcxrchartstandards")
public class SpcXrchartStandardsController {
    @Autowired
    private SpcXrchartStandardsService spcXrchartStandardsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcxrchartstandards:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcXrchartStandardsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcxrId}")
    @RequiresPermissions("spc:spcxrchartstandards:info")
    public R info(@PathVariable("spcxrId") Long spcxrId){
		SpcXrchartStandardsEntity spcXrchartStandards = spcXrchartStandardsService.getById(spcxrId);

        return R.ok().put("spcXrchartStandards", spcXrchartStandards);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcxrchartstandards:save")
    public R save(@RequestBody SpcXrchartStandardsEntity spcXrchartStandards){
		spcXrchartStandardsService.save(spcXrchartStandards);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcxrchartstandards:update")
    public R update(@RequestBody SpcXrchartStandardsEntity spcXrchartStandards){
		spcXrchartStandardsService.updateById(spcXrchartStandards);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcxrchartstandards:delete")
    public R delete(@RequestBody Long[] spcxrIds){
		spcXrchartStandardsService.removeByIds(Arrays.asList(spcxrIds));

        return R.ok();
    }

}
