package io.renren.modules.instrument.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.instrument.service.InstrumentTestMethodService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.instrument.entity.InstrumentationEntity;
import io.renren.modules.instrument.service.InstrumentationService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-31 10:36:37
 */
@RestController
@RequestMapping("generator/instrumentation")
public class InstrumentationController {
    @Autowired
    private InstrumentationService instrumentationService;
    @Autowired
    private InstrumentTestMethodService instrumentTestMethodService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:instrumentation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = instrumentationService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 信息
     */
    @RequestMapping("/linkedMethod/{id}")
    @RequiresPermissions("generator:instrumentation:info")
    public R linkedMethod(@PathVariable("id") Integer id){
        InstrumentationEntity instrumentation = instrumentationService.getById(id);
        System.out.println("看关联的检验方法"+instrumentation);
         String toolName = instrumentation.getToolName();
//        instrumentTestMethodService.get
        return R.ok().put("instrumentation", instrumentation);
    }
    /*
    * 模糊查询
    * */
    @GetMapping("/Fuzzy_queries")
    public R Fuzzy_queries(@RequestParam Map<String, Object> params) {

        PageUtils page = instrumentationService.queryPage(params);

        return R.ok().put("page", page);

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:instrumentation:info")
    public R info(@PathVariable("id") Integer id){
		InstrumentationEntity instrumentation = instrumentationService.getById(id);

        return R.ok().put("instrumentation", instrumentation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:instrumentation:save")
    public R save(@RequestBody InstrumentationEntity instrumentation){
		instrumentationService.save(instrumentation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:instrumentation:update")
    public R update(@RequestBody InstrumentationEntity instrumentation){
		instrumentationService.updateById(instrumentation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:instrumentation:delete")
    public R delete(@RequestBody Integer[] ids){
		instrumentationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
