package io.renren.modules.indicator.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.indicator.entity.IndicatorDataReadEntity;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import io.renren.modules.indicator.service.IndicatorIndicatorSummaryService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


// 自定义监听器，处理读取到的Excel数据
@Slf4j

public class DataReadListener extends AnalysisEventListener<IndicatorIndicatorSummaryEntity> {

    private IndicatorIndicatorSummaryService indicatorIndicatorSummaryService;
    private IndicatorDictionaryService indicatorDictionaryService;
    private Date yearMonth;
    private List<IndicatorDataReadEntity> dataList;
    private boolean hasUndefinedIndicator = false; // 标记是否有未定义的指标

    /**
     * 每次批量插入数据的数量
     */
    private static final int batchSize = 100;

    /**
     * 用于暂存数据的集合，直到数量等于batchSize时就会进行插入操作并清空集合
     */
    private List<IndicatorIndicatorSummaryEntity> batchList = new ArrayList<>();

    public DataReadListener(IndicatorIndicatorSummaryService indicatorIndicatorSummaryService,IndicatorDictionaryService indicatorDictionaryService, Date yearMonth,List<IndicatorDataReadEntity> dataList) {
        this.indicatorIndicatorSummaryService = indicatorIndicatorSummaryService;
        this.yearMonth = yearMonth;
        this.dataList = dataList;
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
                System.out.println("existingEntity======>" + existingEntity);
                // 这里可以根据业务需求将匹配的字段从 existingEntity 赋值给 entity，例如：
                // entity.setIndicatorCreatTime(existingEntity.getIndicatorCreatTime());

                // 设置时间戳字段
                entity.setYearMonth(yearMonth);
                entity.setIndicatorCreatTime(new Date());
                //考核部门可能为空
                if(existingEntity.getAssessmentDepartment() != null){
                    entity.setAssessmentDepartment(existingEntity.getAssessmentDepartment());
                }
                entity.setManagementDepartment(existingEntity.getManagementDepartment());
                entity.setIndicatorValue(existingEntity.getIndicatorPlannedValue());
                entity.setIndicatorClassification(existingEntity.getIndicatorClassification());

                // 解析指标公式并计算指标值
                BigDecimal calculatedValue = calculateIndicatorValue(existingEntity.getIndicatorDefinition(), dataList);
                entity.setIndicatorActualValue(calculatedValue);

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

    private BigDecimal calculateIndicatorValue(String formula, List<IndicatorDataReadEntity> dataList) {
        // 解析公式中的变量名，获取变量对应的值
        Map<String, BigDecimal> variableValues = extractVariableValues(formula, dataList);
        System.out.println("variableValues1======>" + variableValues);

        // 计算指标值
        return evaluateFormula(formula, variableValues);
    }

    //从给定的公式中提取所有的变量名，并从数据列表中查找这些变量的值
    private Map<String, BigDecimal> extractVariableValues(String formula, List<IndicatorDataReadEntity> dataList) {
        Map<String, BigDecimal> variableValues = new HashMap<>();

        // 提取公式中的变量名
        // 正则表达式匹配包含括号的整体字符串
//        Pattern pattern = Pattern.compile("[\\s()，+/-]+（[^）]*）|[^\\s()，+/-]+");
        Pattern pattern = Pattern.compile("[^\\s()，+/-]+（[^）]*）|[^\\s()，+/-]+");
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {
            String var = matcher.group().trim(); // 去除前后空格
            System.out.println("var=======>"+var);
            if (!var.isEmpty()) { // 确保变量名不为空
                BigDecimal value = findValueInDataList(dataList, var);
                if (value != null) {
                    // 去掉括号
                    String cleanVar = var.replaceAll("[（）、()]", "").trim();
                    System.out.println("cleanVar=======>"+cleanVar);
                    variableValues.put(cleanVar, value);
                }
            }
        }
        System.out.println("variableValues2======>" + variableValues);

        return variableValues;
    }

    private boolean isSingleDataItem(String item) {
        int openParentheses = 0;
        int closeParentheses = 0;
        for (char c : item.toCharArray()) {
            if (c == '(') {
                openParentheses++;
            } else if (c == ')') {
                closeParentheses++;
            }
        }
        return openParentheses == closeParentheses && openParentheses <= 1;
    }

    //在数据列表中查找给定变量名的值
    private BigDecimal findValueInDataList(List<IndicatorDataReadEntity> dataList, String variableName) {
        for (IndicatorDataReadEntity data : dataList) {
            if (data.getDataContent().equals(variableName)) {
                return data.getDataValue();
            }
        }
        return null;
    }

    private BigDecimal evaluateFormula(String formula, Map<String, BigDecimal> variableValues) {
        try {
            // 打印传入的公式
            System.out.println("Evaluating formula: " + formula);

            // 将 Map<String, BigDecimal> 转换为 Map<String, Double>
            Map<String, Double> doubleValues = new HashMap<>();
            for (Map.Entry<String, BigDecimal> entry : variableValues.entrySet()) {
                String cleanVar = entry.getKey().replaceAll("[（）、()]", "").trim();
                doubleValues.put(cleanVar, entry.getValue().doubleValue());
                System.out.println("Variable: " + cleanVar + " = " + entry.getValue());
            }

            System.out.println("doubleValues======>" + doubleValues);

            // 构建并计算表达式
            log.info("传入的表达式为: " + formula);
            log.info("传入的变量为: " + doubleValues);
            // 新增处理，去掉表达式中的中文括号
            formula = formula.replaceAll("[（）]", "");
            log.info("修改后的表达式为: " + formula);

            Expression expression = new ExpressionBuilder(formula)
                    .variables(doubleValues.keySet().toArray(new String[0]))
                    .build();
            System.out.println("expression======>" + expression);

            for (Map.Entry<String, Double> entry : doubleValues.entrySet()) {
                expression.setVariable(entry.getKey(), entry.getValue());
            }

            double result = expression.evaluate();
            System.out.println("result======>" + result);
            return BigDecimal.valueOf(result);
        } catch (Exception e) {
            System.err.println("Error while evaluating formula: " + e.getMessage());
            e.printStackTrace();
            return null; // 或者返回特定的错误值
        }
    }

}
