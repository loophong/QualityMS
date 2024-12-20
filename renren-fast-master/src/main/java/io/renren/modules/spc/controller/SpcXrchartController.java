package io.renren.modules.spc.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.service.SpcXrchartService;
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
@RequestMapping("spc/spcxrchart")
public class SpcXrchartController {
    @Autowired
    private SpcXrchartService spcXrchartService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("spc:spcxrchart:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spcXrchartService.queryPage(params);

        System.out.println(page);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{spcxrId}")
    @RequiresPermissions("spc:spcxrchart:info")
    public R info(@PathVariable("spcxrId") Long spcxrId){
		SpcXrchartEntity spcXrchart = spcXrchartService.getById(spcxrId);

        return R.ok().put("spcXrchart", spcXrchart);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("spc:spcxrchart:save")
    public R save(@RequestBody SpcXrchartEntity spcXrchart){
		spcXrchartService.save(spcXrchart);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("spc:spcxrchart:update")
    public R update(@RequestBody SpcXrchartEntity spcXrchart){
		spcXrchartService.updateById(spcXrchart);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("spc:spcxrchart:delete")
    public R delete(@RequestBody Long[] spcxrIds){
		spcXrchartService.removeByIds(Arrays.asList(spcxrIds));

        return R.ok();
    }


    @RequestMapping("/chart/Xbar_R")
    public R getXbar_RChart(@RequestParam String tableName){
        return R.ok().put("Xbar-R_Info", spcXrchartService.getXbar_RChart(tableName));
    }

    @RequestMapping("/chart/Xbar_S")
    public R getXbar_SChart(@RequestParam String tableName){
        return R.ok().put("Xbar-S_Info", spcXrchartService.getXbar_SChart(tableName));
    }

    @RequestMapping("/chart/Me_R")
    public R getMe_RChart(@RequestParam String tableName){
        return R.ok().put("Me-R_Info", spcXrchartService.getMe_RChart(tableName));
    }

    @RequestMapping("/chart/I_MR")
    public R getI_MRChart(@RequestParam String tableName){
        return R.ok().put("I-MR_Info", spcXrchartService.getI_MRChart(tableName));
    }

    @RequestMapping("/options")
    public R getTableName(){
        return R.ok().put("table_name",spcXrchartService.getTableName());
    }
}
