package io.renren.modules.indicator.service.impl;

import com.alibaba.excel.EasyExcel;
import io.renren.common.utils.R;
import io.renren.modules.indicator.listener.DataReadListener;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorIndicatorSummaryDao;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.service.IndicatorIndicatorSummaryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("indicatorIndicatorSummaryService")
public class IndicatorIndicatorSummaryServiceImpl extends ServiceImpl<IndicatorIndicatorSummaryDao, IndicatorIndicatorSummaryEntity> implements IndicatorIndicatorSummaryService {

    @Autowired
    @Lazy
    private IndicatorIndicatorSummaryService indicatorIndicatorSummaryService;
    @Autowired
    private IndicatorDictionaryService indicatorDictionaryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndicatorIndicatorSummaryEntity> page = this.page(
                new Query<IndicatorIndicatorSummaryEntity>().getPage(params),
                new QueryWrapper<IndicatorIndicatorSummaryEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public R readProductionExcelToDB(String fileName, InputStream inputStream, Date yearMonth) {
        try {
            // 使用 EasyExcel 读取文件
            EasyExcel.read(inputStream, IndicatorIndicatorSummaryEntity.class, new DataReadListener(indicatorIndicatorSummaryService,indicatorDictionaryService,yearMonth)).sheet().doRead();

            return R.ok("读取" + fileName + "文件成功");
        } catch (Exception e) {
            log.error("读取 " + fileName + " 文件失败, 原因: {}", e.getMessage());
            return R.error("读取文件失败,您需要上传指标汇总表,当前上传的文件为：" + fileName);
        }
    }
}
