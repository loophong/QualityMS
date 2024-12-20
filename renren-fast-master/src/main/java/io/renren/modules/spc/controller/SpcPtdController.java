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

import io.renren.modules.spc.entity.SpcPtdEntity;
import io.renren.modules.spc.service.SpcPtdService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-03 15:22:22
 */
@RestController
@RequestMapping("spc/spcptd")
public class SpcPtdController {
    @Autowired
    private SpcPtdService spcPtdService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcptd:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcPtdService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("spc:spcptd:info")
    public R info(@PathVariable("id") Long id){
		SpcPtdEntity spcPtd = spcPtdService.getById(id);

        return R.ok().put("spcPtd", spcPtd);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcptd:save")
    public R save(@RequestBody SpcPtdEntity spcPtd){
		spcPtdService.save(spcPtd);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcptd:update")
    public R update(@RequestBody SpcPtdEntity spcPtd){
		spcPtdService.updateById(spcPtd);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcptd:delete")
    public R delete(@RequestBody Long[] ids){
		spcPtdService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/chart/ptd")
    public R getPTDChart(){
        return R.ok().put("ptd-chart_Info", spcPtdService.getPTDChart());
    }

    @RequestMapping("/chart/pValue")
    public R getPTDPValue(){
        return R.ok().put("ptd-p-value", spcPtdService.getPTDPValue());
    }

}
