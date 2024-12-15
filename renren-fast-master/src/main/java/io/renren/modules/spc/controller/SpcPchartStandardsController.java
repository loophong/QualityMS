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

import io.renren.modules.spc.entity.SpcPchartStandardsEntity;
import io.renren.modules.spc.service.SpcPchartStandardsService;
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
@RequestMapping("spc/spcpchartstandards")
public class SpcPchartStandardsController {
    @Autowired
    private SpcPchartStandardsService spcPchartStandardsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcpchartstandards:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcPchartStandardsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcpId}")
    @RequiresPermissions("spc:spcpchartstandards:info")
    public R info(@PathVariable("spcpId") Long spcpId){
		SpcPchartStandardsEntity spcPchartStandards = spcPchartStandardsService.getById(spcpId);

        return R.ok().put("spcPchartStandards", spcPchartStandards);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcpchartstandards:save")
    public R save(@RequestBody SpcPchartStandardsEntity spcPchartStandards){
		spcPchartStandardsService.save(spcPchartStandards);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcpchartstandards:update")
    public R update(@RequestBody SpcPchartStandardsEntity spcPchartStandards){
		spcPchartStandardsService.updateById(spcPchartStandards);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcpchartstandards:delete")
    public R delete(@RequestBody Long[] spcpIds){
		spcPchartStandardsService.removeByIds(Arrays.asList(spcpIds));

        return R.ok();
    }

}
