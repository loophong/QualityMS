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

import io.renren.modules.instrument.entity.InstrumentTestMethodEntity;
import io.renren.modules.instrument.service.InstrumentTestMethodService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-31 10:55:46
 */
@RestController
@RequestMapping("generator/instrumenttestmethod")
public class InstrumentTestMethodController {
    @Autowired
    private InstrumentTestMethodService instrumentTestMethodService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:instrumenttestmethod:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentTestMethodService.queryPage(params);
        System.out.println("测试检验方法"+page);
        return R.ok().put("page", page);
    }

    /**
     * 审核
     */
    @RequestMapping("/success/{id}")
//    @RequiresPermissions("generator:instrumenttestmethod:success")
    public R verify(@PathVariable("id") Integer id){
        InstrumentTestMethodEntity instrumentTestMethod = instrumentTestMethodService.getById(id);
        instrumentTestMethod.setFlag("1");
        System.out.println("测试审核id"+id+instrumentTestMethod);
        instrumentTestMethodService.saveOrUpdate(instrumentTestMethod);

        return R.ok().put("instrumentTestMethod", instrumentTestMethod);
    }

    /**
     * 审核不通过
     */
    @RequestMapping("/fail/{id}")
//    @RequiresPermissions("generator:instrumenttestmethod:success")
    public R verifyfail(@PathVariable("id") Integer id){
        InstrumentTestMethodEntity instrumentTestMethod = instrumentTestMethodService.getById(id);
        instrumentTestMethod.setFlag("2");
        System.out.println("测试审核id"+id+instrumentTestMethod);
        instrumentTestMethodService.saveOrUpdate(instrumentTestMethod);

        return R.ok().put("instrumentTestMethod", instrumentTestMethod);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:instrumenttestmethod:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentTestMethodEntity instrumentTestMethod = instrumentTestMethodService.getById(id);

        return R.ok().put("instrumentTestMethod", instrumentTestMethod);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:instrumenttestmethod:save")
    public R save(@RequestBody InstrumentTestMethodEntity instrumentTestMethod){
		instrumentTestMethodService.save(instrumentTestMethod);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:instrumenttestmethod:update")
    public R update(@RequestBody InstrumentTestMethodEntity instrumentTestMethod){
		instrumentTestMethodService.updateById(instrumentTestMethod);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:instrumenttestmethod:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentTestMethodService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
