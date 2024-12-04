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

import io.renren.modules.spc.entity.SpcPchartEntity;
import io.renren.modules.spc.service.SpcPchartService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
@RestController
@RequestMapping("spc/spcpchart")
public class SpcPchartController {
    @Autowired
    private SpcPchartService spcPchartService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcpchart:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcPchartService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcpId}")
    @RequiresPermissions("spc:spcpchart:info")
    public R info(@PathVariable("spcpId") Long spcpId){
		SpcPchartEntity spcPchart = spcPchartService.getById(spcpId);

        return R.ok().put("spcPchart", spcPchart);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcpchart:save")
    public R save(@RequestBody SpcPchartEntity spcPchart){
		spcPchartService.save(spcPchart);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcpchart:update")
    public R update(@RequestBody SpcPchartEntity spcPchart){
		spcPchartService.updateById(spcPchart);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcpchart:delete")
    public R delete(@RequestBody Long[] spcpIds){
		spcPchartService.removeByIds(Arrays.asList(spcpIds));

        return R.ok();
    }

    @RequestMapping("/chart/p")
    public R getPChart(){
        return R.ok().put("p-chart_Info", spcPchartService.getPChart());
    }

    @RequestMapping("/chart/np")
    public R getNPChart(){
        return R.ok().put("np-chart_Info", spcPchartService.getNPChart());
    }

    @RequestMapping("/chart/u")
    public R getUChart(){
        return R.ok().put("u-chart_Info", spcPchartService.getUChart());
    }

    @RequestMapping("/chart/c")
    public R getCChart(){
        return R.ok().put("c-chart_Info", spcPchartService.getCChart());
    }

}
