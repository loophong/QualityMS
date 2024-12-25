package io.renren.modules.indicator.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.indicator.entity.IndicatorChart1Entity;
import io.renren.modules.indicator.entity.IndicatorChart2Entity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.entity.IndicatorYearDataEntity;
import io.renren.modules.indicator.listener.DataReadListener;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-20 14:17:04
 */
public interface IndicatorIndicatorSummaryService extends IService<IndicatorIndicatorSummaryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<IndicatorIndicatorSummaryEntity> queryList(Map<String, Object> params);

    PageUtils queryIndicatorList(Map<String, Object> params);

    public R readProductionExcelToDB(String fileName, InputStream inputStream, InputStream inputStream2, Date yearMonth);

    public List<IndicatorChart1Entity> queryChart1List(IndicatorChart1Entity indicatorChart1Entity);

    public List<IndicatorChart2Entity> queryChart2List(IndicatorChart2Entity indicatorChart2Entity);

    public List<IndicatorYearDataEntity> queryYearDataList(IndicatorIndicatorSummaryEntity indicatorIndicatorSummaryEntity);
}

