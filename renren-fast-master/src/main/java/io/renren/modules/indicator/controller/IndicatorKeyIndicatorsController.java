package io.renren.modules.indicator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.indicator.service.IndicatorKeyIndicatorsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-21 18:47:20
 */
@Slf4j
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
    @RequestMapping("/list01")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public List<IndicatorKeyIndicatorsEntity> list01(@RequestParam Map<String, Object> params){
        List<IndicatorKeyIndicatorsEntity> list = indicatorKeyIndicatorsService.queryList(params);

        return list;
    }
   /*
   * 指标知识库
   * */
//    @RequestMapping("/list1")
//    @RequiresPermissions("indicator:indicatorkeyindicators:list")
//    public R list1(@RequestParam Map<String, Object> params) {
//        PageUtils page = indicatorKeyIndicatorsService.queryPageFinishedList(params);
//
//        return R.ok().put("page", page);
//    }
    @RequestMapping("/list1")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public R list1(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorKeyIndicatorsService.queryPage1(params);

        return R.ok().put("page", page);
    }
/*
    @RequestMapping("/list1")
    @RequiresPermissions("indicator:indicatorkeyindicators:list")
    public R list1(@RequestParam Map<String, Object> params) {
        PageUtils page = indicatorKeyIndicatorsService.queryPageFinishedList(params);

        return R.ok().put("page", page);
    }*/


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


    @PostMapping("/clearStorageFlag")
//    @RequiresPermissions("indicator:indicatorkeyindicators:clearStorageFlag")
    public R clearStorageFlag(@RequestBody Integer[] keyIndicatorIds) {
        if (keyIndicatorIds == null || keyIndicatorIds.length == 0) {
            return R.error("Invalid input: keyIndicatorIds cannot be null or empty");
        }

        try {
            List<Integer> ids = Arrays.asList(keyIndicatorIds);
            System.out.println("ids===>"+ids);
            boolean result = indicatorKeyIndicatorsService.clearStorageFlag(ids);
            if (result) {
                return R.ok();
            } else {
                return R.error("Failed to clear storage_flag");
            }
        } catch (Exception e) {
            // 记录日志
            e.printStackTrace();
            return R.error("An error occurred while clearing storage_flag: " + e.getMessage());
        }
    }

}





