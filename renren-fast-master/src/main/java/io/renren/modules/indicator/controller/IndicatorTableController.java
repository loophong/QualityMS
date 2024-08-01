package io.renren.modules.indicator.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.indicator.entity.IndicatorTableEntity;
import io.renren.modules.indicator.service.IndicatorTableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 指标表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 14:24:18
 */
@RestController
@RequestMapping("indicator/indicatortable")
public class IndicatorTableController {
    @Autowired
    private IndicatorTableService indicatorTableService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("indicator:indicatortable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorTableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("indicator:indicatortable:info")
    public R info(@PathVariable("id") Integer id){
		IndicatorTableEntity indicatorTable = indicatorTableService.getById(id);

        return R.ok().put("indicatorTable", indicatorTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("indicator:indicatortable:save")
    public R save(@RequestBody IndicatorTableEntity indicatorTable){
		indicatorTableService.save(indicatorTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("indicator:indicatortable:update")
    public R update(@RequestBody IndicatorTableEntity indicatorTable){
		indicatorTableService.updateById(indicatorTable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("indicator:indicatortable:delete")
    public R delete(@RequestBody Integer[] ids){
		indicatorTableService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
