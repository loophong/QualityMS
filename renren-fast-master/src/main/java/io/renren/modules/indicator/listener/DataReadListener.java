package io.renren.modules.indicator.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import io.renren.modules.indicator.service.IndicatorIndicatorSummaryService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 自定义监听器，处理读取到的Excel数据
@Slf4j

public class DataReadListener extends AnalysisEventListener<IndicatorIndicatorSummaryEntity> {

    private IndicatorIndicatorSummaryService indicatorIndicatorSummaryService;
    private IndicatorDictionaryService indicatorDictionaryService;
    private Date yearMonth;
    private boolean hasUndefinedIndicator = false; // 标记是否有未定义的指标

    /**
     * 每次批量插入数据的数量
     */
    private static final int batchSize = 100;

    /**
     * 用于暂存数据的集合，直到数量等于batchSize时就会进行插入操作并清空集合
     */
    private List<IndicatorIndicatorSummaryEntity> batchList = new ArrayList<>();

    public DataReadListener(IndicatorIndicatorSummaryService indicatorIndicatorSummaryService,IndicatorDictionaryService indicatorDictionaryService, Date yearMonth) {
        this.indicatorIndicatorSummaryService = indicatorIndicatorSummaryService;
        this.yearMonth = yearMonth;
        this.indicatorDictionaryService = indicatorDictionaryService;
    }

    @Override
    public void invoke(IndicatorIndicatorSummaryEntity entity, AnalysisContext analysisContext) {
        // 处理空行数据
        if (isEmptyRow(entity)) {
            log.info("忽略空行数据");
            return;
        }
        log.info("读取到的行数据：{}", entity);

        try {
            // 根据指标名称查询数据库中的匹配记录
            List<IndicatorDictionaryEntity> resultList = indicatorDictionaryService.getDictionaryEntitiesByName(entity.getIndicatorName());

            // 如果在指标字典中找到匹配记录，处理数据
            if (!resultList.isEmpty()) {
                IndicatorDictionaryEntity existingEntity = resultList.get(0);
                // 这里可以根据业务需求将匹配的字段从 existingEntity 赋值给 entity，例如：
                // entity.setIndicatorCreatTime(existingEntity.getIndicatorCreatTime());

                // 设置时间戳字段
                entity.setYearMonth(yearMonth);
                entity.setIndicatorCreatTime(new Date());

                // 处理空单元格
                setNullValues(entity);

                // 数据校验
                if (validateData(entity)) {
                    batchList.add(entity);
                } else {
                    // 没有通过校验的数据，打印日志
                    log.error("id为[{}]的数据没有通过校验", entity.getIndicatorId());
                }

                // 如果集合数量达到批量插入的数量限制，则进行批量插入操作
                if (batchList.size() >= batchSize) {
                    indicatorIndicatorSummaryService.saveBatch(batchList);
                    batchList.clear();
                }
            } else {
                // 如果在指标字典中没有找到匹配的记录，则忽略该行
                log.info("指标字典中未找到名称为[{}]的记录，忽略该行", entity.getIndicatorName());
                hasUndefinedIndicator = true; // 标记为true表示存在未定义的指标
            }
        } catch (Exception e) {
            // 捕获并打印异常信息
            log.error("查询指标字典时出错：{}", e.getMessage(), e);
        }
    }
//
//    // EasyExcel每读取一行数据就会执行一次
//    @Override
//    public void invoke(IndicatorIndicatorSummaryEntity entity, AnalysisContext analysisContext) {
//        // 处理空行数据
//        if (isEmptyRow(entity)) {
//            log.info("忽略空行数据");
//            return;
//        }
//        log.info("读取到的行数据：{}", entity);
//
//        try {
//            // 查询数据库中指标名称相同的记录
//            List<IndicatorDictionaryEntity> resultList = indicatorDictionaryService.getDictionaryEntitiesByName(entity.getIndicatorName());
//            System.out.println("resultList========>" + resultList);
//            if (!resultList.isEmpty()) {
//                //将读取到的entity与指标字典进行匹配，成功则将entity某些字段的值设为字典中匹配到的记录的对应值
//                QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();
//                System.out.println("queryWrapper=====>" + queryWrapper);
//                queryWrapper.eq("indicator_name", entity.getIndicatorName());
//                System.out.println("entity.getIndicatorName()====>" + entity.getIndicatorName());
//                IndicatorDictionaryEntity existingEntity = resultList.get(0);
////                entity.setIndicatorCreatTime(existingEntity.getIndicatorCreatTime());
//                entity.setYearMonth(yearMonth);
//                entity.setIndicatorCreatTime(new Date());
//                //处理空单元格，赋予空值
//                setNullValues(entity);
//                if (validateData(entity)) {
//                    batchList.add(entity);
//                } else {
//                    // 没有通过校验的数据，打印日志
//                    log.error("id为[{}]的数据没有通过校验", entity.getIndicatorId());
//                }
//
//                // 如果集合数量大于设置的批量数量，那么就插入数据并清空集合
//                if (batchList.size() >= batchSize) {
//                    // 批量插入数据
//                    indicatorIndicatorSummaryService.saveBatch(batchList);
//                    batchList.clear();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // 打印堆栈信息
//            log.error("查询指标字典时出错：{}", e.getMessage(), e);
//        }
//    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("Excel读取完成！");
        // 如果还有数据就一起插入到数据库中
        if (!batchList.isEmpty()) {
            indicatorIndicatorSummaryService.saveBatch(batchList);;
        }
    }

    // 数据校验逻辑，返回true表示数据有效，返回false表示数据无效
    private boolean validateData(IndicatorIndicatorSummaryEntity entity) {
        // 在这里添加你的数据校验逻辑
        return true;
    }


    // 通过反射设置默认值为null
    private void setNullValues(IndicatorIndicatorSummaryEntity entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(entity) == null) {
                    field.set(entity, null);
                }
            } catch (IllegalAccessException e) {
                log.error("设置默认值失败: {}", e.getMessage());
            }
        }
    }

    private boolean isEmptyRow(IndicatorIndicatorSummaryEntity entity) {
        // 检查实体的所有字段是否都为空
        return entity.getIndicatorId() == null &&
                entity.getIndicatorName() == null &&
                entity.getIndicatorValue() == null &&
                entity.getIndicatorValueUpperBound() == null &&
                entity.getIndicatorValueLowerBound() == null &&
                entity.getAssessmentDepartment() == null &&
                entity.getManagementDepartment() == null &&
                entity.getIndicatorDefinition() == null &&
                entity.getIndicatorClassification() == null &&
                entity.getManagementContentCurrentAnalysis() == null &&
                entity.getDataId() == null &&
                entity.getSourceDepartment() == null &&
                entity.getCollectionMethod() == null &&
                entity.getCollectionFrequency() == null &&
                entity.getPlanId() == null &&
                entity.getTaskId() == null &&
                entity.getIndicatorParentNode() == null &&
                entity.getIndicatorCreatTime() == null &&
                entity.getYearMonth() == null &&
                entity.getIndicatorState() == null &&
                entity.getIndicatorChildNode() == null;
    }

    // 判断是否有未定义的指标
    public boolean hasUndefinedIndicator() {
        return hasUndefinedIndicator;
    }
}
