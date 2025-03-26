package io.renren.modules.instrument.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.instrument.entity.InstrumentClassifyEntity;
import io.renren.modules.instrument.service.InstrumentClassifyService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-16 16:51:59
 */
@RestController
@RequestMapping("generator/instrumentclassify")
public class InstrumentClassifyController {
    @Autowired
    private InstrumentClassifyService instrumentClassifyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("generator:instrumentclassify:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentClassifyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:instrumentclassify:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentClassifyEntity instrumentClassify = instrumentClassifyService.getById(id);

        return R.ok().put("instrumentClassify", instrumentClassify);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:instrumentclassify:save")
    public R save(@RequestBody InstrumentClassifyEntity instrumentClassify){
		instrumentClassifyService.save(instrumentClassify);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:instrumentclassify:update")
    public R update(@RequestBody InstrumentClassifyEntity instrumentClassify){
		instrumentClassifyService.updateById(instrumentClassify);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:instrumentclassify:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentClassifyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
