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

import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDataDictionaryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lin
 * @email ${email}
 * @date 2024-08-14 16:51:50
 */
@RestController
@RequestMapping("indicator/indicatordatadictionary")
public class IndicatorDataDictionaryController {
    @Autowired
    private IndicatorDataDictionaryService indicatorDataDictionaryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("indicator:indicatordatadictionary:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorDataDictionaryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dataId}")
    @RequiresPermissions("indicator:indicatordatadictionary:info")
    public R info(@PathVariable("dataId") Integer dataId){
		IndicatorDataDictionaryEntity indicatorDataDictionary = indicatorDataDictionaryService.getById(dataId);

        return R.ok().put("indicatorDataDictionary", indicatorDataDictionary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("indicator:indicatordatadictionary:save")
    public R save(@RequestBody IndicatorDataDictionaryEntity indicatorDataDictionary){
		indicatorDataDictionaryService.save(indicatorDataDictionary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("indicator:indicatordatadictionary:update")
    public R update(@RequestBody IndicatorDataDictionaryEntity indicatorDataDictionary){
		indicatorDataDictionaryService.updateById(indicatorDataDictionary);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("indicator:indicatordatadictionary:delete")
    public R delete(@RequestBody Integer[] dataIds){
		indicatorDataDictionaryService.removeByIds(Arrays.asList(dataIds));

        return R.ok();
    }

}
