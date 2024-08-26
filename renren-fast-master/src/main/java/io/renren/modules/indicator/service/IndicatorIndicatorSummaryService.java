package io.renren.modules.indicator.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.listener.DataReadListener;

import java.io.InputStream;
import java.util.Date;
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

    PageUtils queryIndicatorList(Map<String, Object> params);

    public R readProductionExcelToDB(String fileName, InputStream inputStream, Date yearMonth);

}

