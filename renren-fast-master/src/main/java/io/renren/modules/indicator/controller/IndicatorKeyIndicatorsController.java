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

import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.indicator.service.IndicatorKeyIndicatorsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 18:47:20
 */
@RestController
@RequestMapping("indicator/indicatorkeyindicators")
public class IndicatorKeyIndicatorsController {
    @Autowired
    private IndicatorKeyIndicatorsService indicatorKeyIndicatorsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = indicatorKeyIndicatorsService.queryPage(params);

        return R.ok().put("page", page);
    }
   /*
   * 指标知识库
   * */
    @RequestMapping("/list1")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public R list1(@RequestParam Map<String, Object> params) {
        PageUtils page = indicatorKeyIndicatorsService.queryPageFinishedList(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/list1")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public R list1(@RequestParam Map<String, Object> params) {
        PageUtils page = indicatorKeyIndicatorsService.queryPageFinishedList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{keyIndicatorId}")
    @RequiresPermissions("indicator:indicatorkeyindicators:info")
    public R info(@PathVariable("keyIndicatorId") Integer keyIndicatorId) {
        IndicatorKeyIndicatorsEntity indicatorKeyIndicators = indicatorKeyIndicatorsService.getById(keyIndicatorId);

        return R.ok().put("indicatorKeyIndicators", indicatorKeyIndicators);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("indicator:indicatorkeyindicators:save")
    public R save(@RequestBody IndicatorKeyIndicatorsEntity indicatorKeyIndicators) {
        indicatorKeyIndicatorsService.save(indicatorKeyIndicators);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("indicator:indicatorkeyindicators:update")
    public R update(@RequestBody IndicatorKeyIndicatorsEntity indicatorKeyIndicators) {
        indicatorKeyIndicatorsService.updateById(indicatorKeyIndicators);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("indicator:indicatorkeyindicators:delete")
    public R delete(@RequestBody Integer[] keyIndicatorIds) {
        indicatorKeyIndicatorsService.removeByIds(Arrays.asList(keyIndicatorIds));

        return R.ok();
    }

}
