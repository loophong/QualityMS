package io.renren.modules.indicator.controller;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aliyun.oss.ServiceException;
import io.renren.modules.indicator.entity.IndicatorChart1Entity;
import io.renren.modules.indicator.entity.IndicatorChart2Entity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.service.IndicatorIndicatorSummaryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-20 14:17:04
 */
@RestController
@RequestMapping("indicator/indicatorindicatorsummary")
public class IndicatorIndicatorSummaryController {
    @Autowired
    private IndicatorIndicatorSummaryService indicatorIndicatorSummaryService;

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("indicator:indicatorindicatorsummary:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorIndicatorSummaryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询列表（不分页）
     */
    @RequestMapping("/list01")
    @RequiresPermissions("indicator:indicatorindicatorsummary:list")
    public List<IndicatorIndicatorSummaryEntity> list01(@RequestParam Map<String, Object> params){
        List<IndicatorIndicatorSummaryEntity> list = indicatorIndicatorSummaryService.queryList(params);

        return list;
    }

    /**
     * 查询时间范围内的指标列表
     */
    @RequestMapping("/indicatorlist")
    @RequiresPermissions("indicator:indicatorindicatorsummary:list")
    public R indicatorList(@RequestParam Map<String, Object> params){
        PageUtils page = indicatorIndicatorSummaryService.queryIndicatorList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{indicatorId}")
    @RequiresPermissions("indicator:indicatorindicatorsummary:info")
    public R info(@PathVariable("indicatorId") Integer indicatorId){
		IndicatorIndicatorSummaryEntity indicatorIndicatorSummary = indicatorIndicatorSummaryService.getById(indicatorId);

        return R.ok().put("indicatorIndicatorSummary", indicatorIndicatorSummary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("indicator:indicatorindicatorsummary:save")
    public R save(@RequestBody IndicatorIndicatorSummaryEntity indicatorIndicatorSummary){
		indicatorIndicatorSummaryService.save(indicatorIndicatorSummary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("indicator:indicatorindicatorsummary:update")
    public R update(@RequestBody IndicatorIndicatorSummaryEntity indicatorIndicatorSummary){
		indicatorIndicatorSummaryService.updateById(indicatorIndicatorSummary);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("indicator:indicatorindicatorsummary:delete")
    public R delete(@RequestBody Integer[] indicatorIds){
		indicatorIndicatorSummaryService.removeByIds(Arrays.asList(indicatorIds));

        return R.ok();
    }

    /**
     * 上传excel
     */
    public R simpleRead(@RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("yearMonth") @DateTimeFormat(pattern = "yyyy-MM") Date yearMonth) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            InputStream inputStream2 = multipartFile.getInputStream();

            // 调用服务方法，并获取返回结果
            R result = indicatorIndicatorSummaryService.readProductionExcelToDB(multipartFile.getOriginalFilename(), inputStream,inputStream2, yearMonth);

            // 根据服务方法的返回结果决定返回给前端的消息
            return result;
        } catch (Exception e) {
            System.out.println("读取 " + multipartFile.getName() + " 文件失败, 原因: " + e.getMessage());
            throw new ServiceException("读取 " + multipartFile.getName() + " 文件失败", e);
        }
    }

    /**
     * 查询图表1数据
     */
    @RequestMapping("/chart1")
    public List<IndicatorChart1Entity> queryChart1(IndicatorChart1Entity indicatorChart1Entity){
    	List<IndicatorChart1Entity> list = indicatorIndicatorSummaryService.queryChart1List(indicatorChart1Entity);
        // 打印 list 中的每个元素
        for (IndicatorChart1Entity entity : list) {
            System.out.println(entity);
        }
    	return list;
    }

    /**
     * 查询图表2数据
     */
    @RequestMapping("/chart2")
    public List<IndicatorChart2Entity> queryChart2(IndicatorChart2Entity indicatorChart2Entity){
        List<IndicatorChart2Entity> list = indicatorIndicatorSummaryService.queryChart2List(indicatorChart2Entity);
        // 打印 list 中的每个元素
        for (IndicatorChart2Entity entity : list) {
            System.out.println(entity);
        }
        return list;
    }


}
