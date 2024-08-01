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

import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 17:55:09
 */
@RestController
@RequestMapping("indicator/indicatordictionary")
public class IndicatorDictionaryController {
    @Autowired
    private IndicatorDictionaryService indicatorDictionaryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("indicator:indicatordictionary:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorDictionaryService.queryPage(params);


        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{indicatorId}")
    @RequiresPermissions("indicator:indicatordictionary:info")
    public R info(@PathVariable("indicatorId") Integer indicatorId){
		IndicatorDictionaryEntity indicatorDictionary = indicatorDictionaryService.getById(indicatorId);

        return R.ok().put("indicatorDictionary", indicatorDictionary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("indicator:indicatordictionary:save")
    public R save(@RequestBody IndicatorDictionaryEntity indicatorDictionary){
		indicatorDictionaryService.save(indicatorDictionary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("indicator:indicatordictionary:update")
    public R update(@RequestBody IndicatorDictionaryEntity indicatorDictionary){
		indicatorDictionaryService.updateById(indicatorDictionary);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("indicator:indicatordictionary:delete")
    public R delete(@RequestBody Integer[] indicatorIds){
		indicatorDictionaryService.removeByIds(Arrays.asList(indicatorIds));

        return R.ok();
    }

}
