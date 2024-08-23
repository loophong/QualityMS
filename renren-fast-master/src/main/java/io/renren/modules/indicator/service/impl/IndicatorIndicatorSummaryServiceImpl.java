package io.renren.modules.indicator.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorIndicatorSummaryEntity> queryWrapper = new QueryWrapper<>();

        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "indicatorName":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getIndicatorName, value);
                                break;
                            case "indicatorValue":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getIndicatorValue, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getManagementDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getAssessmentDepartment, value);
                                break;
                            // 继续为其他字段添加查询条件...
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // 处理 JSON 解析异常
            }
        }

        IPage<IndicatorIndicatorSummaryEntity> page = this.page(
                new Query<IndicatorIndicatorSummaryEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }




    @Override
    public R readProductionExcelToDB(String fileName, InputStream inputStream, Date yearMonth) {
        try {
            DataReadListener listener = new DataReadListener(indicatorIndicatorSummaryService, indicatorDictionaryService, yearMonth);
            EasyExcel.read(inputStream, IndicatorIndicatorSummaryEntity.class, listener).sheet().doRead();

            if (listener.hasUndefinedIndicator()) {
                return R.ok("读取" + fileName + "文件成功，但存在未定义的指标，已自动忽略");
            } else {
                return R.ok("读取" + fileName + "文件成功");
            }
        } catch (Exception e) {
            log.error("读取 " + fileName + " 文件失败, 原因: {}", e.getMessage());
            return R.error("读取文件失败,您需要上传指标汇总表,当前上传的文件为：" + fileName);
        }
    }

}
