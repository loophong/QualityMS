package io.renren.modules.indicator.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.R;
import io.renren.modules.indicator.entity.*;
import io.renren.modules.indicator.listener.DataReadListener;
import io.renren.modules.indicator.listener.DataReadListener2;
import io.renren.modules.indicator.service.IndicatorDictionaryService;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorIndicatorSummaryDao;
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
    @Autowired
    private IndicatorIndicatorSummaryDao indicatorIndicatorSummaryDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorIndicatorSummaryEntity> queryWrapper = new QueryWrapper<>();
////        排序的话要将各个月份的分成几个组，然后在组间排序
//        queryWrapper.orderByAsc("`year_month`");

        if (key != null && !key.isEmpty()) {
            try {
                // 将 key 字符串解析为 Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> keyMap = objectMapper.readValue(key, Map.class);

                // 遍历 Map 并添加查询条件
                for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();

                    System.out.println("field: " + field + ", value: " + value);

                    if (value != null && !value.isEmpty()) {
                        switch (field) {
                            case "indicatorName":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getIndicatorName, value);
                                break;
                            case "indicatorValue":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getIndicatorValue, value);
                                break;
                            case "startTime":
                                // 添加 startTime 大于等于条件
                                queryWrapper.lambda().ge(IndicatorIndicatorSummaryEntity::getYearMonth, value);
                                break;
                            case "endTime":
                                // 添加 endTime 小于等于条件
                                queryWrapper.lambda().le(IndicatorIndicatorSummaryEntity::getYearMonth, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getManagementDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getAssessmentDepartment, value);
                                break;
                            case "yearMonth":
                                queryWrapper.lambda().like(IndicatorIndicatorSummaryEntity::getYearMonth, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        queryWrapper.orderByDesc("`year_month`").orderByDesc("indicator_id");

        IPage<IndicatorIndicatorSummaryEntity> page = this.page(
                new Query<IndicatorIndicatorSummaryEntity>().getPage(params),
                queryWrapper
        );


        return new PageUtils(page);
    }

    @Override
    public List<IndicatorIndicatorSummaryEntity> queryList(Map<String, Object> params) {
        QueryWrapper<IndicatorIndicatorSummaryEntity> queryWrapper = new QueryWrapper<>();
        return list(queryWrapper);
    };


    @Override
    public PageUtils queryIndicatorList(Map<String, Object> params) {
        System.out.println("params:" + params);
        // 获取 key 参数
        String keyStr = (String) params.get("key");
        Map<String, Object> keyParams = new HashMap<>();

        if (keyStr != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                keyParams = objectMapper.readValue(keyStr, Map.class);
            } catch (Exception e) {
                throw new RuntimeException("解析 key 参数时出错", e);
            }
        }
        // 从 keyParams 中获取具体的查询条件
        String indicatorName = (String) keyParams.get("indicatorName");
        String startTimeStr = (String) keyParams.get("startTime");
        String endTimeStr = (String) keyParams.get("endTime");

        // 定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date startTime = null;
        Date endTime = null;

        try {
            if (startTimeStr != null) {
                startTime = sdf.parse(startTimeStr);
            }
            if (endTimeStr != null) {
                endTime = sdf.parse(endTimeStr);
            }
        } catch (Exception e) {
            throw new RuntimeException("日期解析错误", e);
        }
        System.out.println("indicatorName:" +indicatorName + "startTime:" + startTime + ",endTime:" + endTime);
        QueryWrapper<IndicatorIndicatorSummaryEntity> queryWrapper = new QueryWrapper<>();
        //数据库查询语句
        queryWrapper.like(StringUtils.isNotBlank(indicatorName), "indicator_name", indicatorName)
                .between((startTime != null && endTime != null), "`year_month`", startTime, endTime)
                .orderByAsc("`year_month`");  // 根据年月降序排列

        // 分页查询
        IPage<IndicatorIndicatorSummaryEntity> page = this.page(
                new Query<IndicatorIndicatorSummaryEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public R readProductionExcelToDB(String fileName, InputStream inputStream, InputStream inputStream2, Date yearMonth) {



        try {
            //读取第一次，获取数据、数据值
            DataReadListener2 listener2 = new DataReadListener2();
            EasyExcel.read(inputStream2, IndicatorDataReadEntity.class, listener2).sheet().doRead();
            List<IndicatorDataReadEntity> allDataList =listener2.getBatchList();
            System.out.println("allDataList======>" + allDataList);
            // 使用 HashSet 去重
            Set<IndicatorDataReadEntity> uniqueSet = new HashSet<>(allDataList);
            // 转换回 List
            List<IndicatorDataReadEntity> dataList = new ArrayList<>(uniqueSet);
            System.out.println("dataList======>" + dataList);
            //读取第二次，获取指标名称、指标目标值，根据指标公式计算指标值
            DataReadListener listener = new DataReadListener(indicatorIndicatorSummaryService, indicatorDictionaryService, yearMonth, dataList);
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

    // 将 InputStream 转换为 byte 数组
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }
    // 安全关闭输入流
    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // 忽略关闭时发生的异常
            }
        }
    }

    @Override
    public List<IndicatorChart1Entity> queryChart1List(IndicatorChart1Entity indicatorChart1Entity) {
        return indicatorIndicatorSummaryDao.queryChart1List(indicatorChart1Entity);
    }

    @Override
    public List<IndicatorChart2Entity> queryChart2List(IndicatorChart2Entity indicatorChart2Entity) {
        return indicatorIndicatorSummaryDao.queryChart2List(indicatorChart2Entity);
    }

    @Override
    public List<IndicatorYearDataEntity> queryYearDataList(IndicatorIndicatorSummaryEntity indicatorIndicatorSummaryEntity) {
        // 查询当前年份的数据
        List<IndicatorIndicatorSummaryEntity> list = indicatorIndicatorSummaryDao.queryYearDataList(indicatorIndicatorSummaryEntity);
        if (list == null) {
            list = new ArrayList<>();
        }
        for (IndicatorIndicatorSummaryEntity entity : list) {
            System.out.println(entity);
        }

        // 使用 Map 来存储每个指标的完成情况
        Map<String, BigDecimal> yearDataMap = new HashMap<>();
        Map<String, BigDecimal> targetValuesMap = new HashMap<>();
        for (IndicatorIndicatorSummaryEntity entity : list) {
            String indicatorName = entity.getIndicatorName();
            Date yearMonth = entity.getYearMonth(); // 提取年份和月份
            if (yearMonth != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(yearMonth);
                int year = calendar.get(Calendar.YEAR); // 提取年份
                // 构建 Map 的键
                String key = year + "-" + indicatorName;
                BigDecimal indicatorActualValue = entity.getIndicatorActualValue() != null ? entity.getIndicatorActualValue() : BigDecimal.ZERO;
                BigDecimal indicatorValue = entity.getIndicatorValue() != null ? entity.getIndicatorValue() : BigDecimal.ZERO;
                // 获取当前的累计值，如果没有则初始化为 BigDecimal.ZERO
                BigDecimal indicatorYearActualValue = yearDataMap.getOrDefault(key, BigDecimal.ZERO);
                // 累加月度完成值
                indicatorYearActualValue = indicatorYearActualValue.add(indicatorActualValue);
                // 获取当前的累计值，如果没有则初始化为 BigDecimal.ZERO
                BigDecimal indicatorYearValue = targetValuesMap.getOrDefault(key, BigDecimal.ZERO);

                // 累加月度完成值
                indicatorYearValue = indicatorYearValue.add(indicatorValue);
                // 更新 Map
                yearDataMap.put(key, indicatorYearActualValue);
                targetValuesMap.put(key, indicatorYearValue);
            }
        }

        // 打印 yearDataMap
        for (Map.Entry<String, BigDecimal> entry : yearDataMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        for (Map.Entry<String, BigDecimal> entry : targetValuesMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", targetValue: " + entry.getValue());
        }

        // 创建一个 Map 来存储每个指标的 IndicatorYearDataEntity
        Map<String, IndicatorYearDataEntity> yearDataEntitiesMap = new HashMap<>();
        // 遍历 yearDataMap 进行比较
        for (Map.Entry<String, BigDecimal> entry : yearDataMap.entrySet()) {
            String key = entry.getKey();
            System.out.println("Key: " + key);
            BigDecimal actualValue = entry.getValue() != null ? entry.getValue() : BigDecimal.ZERO;
            String[] parts = key.split("-");
            int year = Integer.parseInt(parts[0]);
            String indicatorName = parts[1];

            BigDecimal targetValue = targetValuesMap.get(key);
            System.out.println("targetValue: " + targetValue);
            if (targetValue != null) {

                // 获取或创建 IndicatorYearDataEntity
                String entityKey = year + "-" + indicatorName;
                IndicatorYearDataEntity yearDataEntity = yearDataEntitiesMap.getOrDefault(entityKey, new IndicatorYearDataEntity());
                // 将 int 类型的 year 转换为 Date 类型
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(year, Calendar.JANUARY, 1); // 设置为该年的1月1日
                Date yearDate = calendar.getTime();

                yearDataEntity.setYear(yearDate);

                // 比较实际值和目标值
                if (actualValue.compareTo(targetValue) >= 0) {
                    yearDataEntity.setFinishedCounts(yearDataEntity.getFinishedCounts() + 1);
                } else {
                    yearDataEntity.setUnfinishedCounts(yearDataEntity.getUnfinishedCounts() + 1);
                }

                // 更新 Map
                yearDataEntitiesMap.put(entityKey, yearDataEntity);
            } else {
                log.warn("No target value found for key: {}", key);
            }
        }

        // 打印 yearDataEntitiesMap
        for (Map.Entry<String, IndicatorYearDataEntity> entry : yearDataEntitiesMap.entrySet()) {
            IndicatorYearDataEntity yearDataEntity = entry.getValue();
            System.out.println("Year: " + yearDataEntity.getYear() +
                    ", Finished Counts: " + yearDataEntity.getFinishedCounts() +
                    ", Unfinished Counts: " + yearDataEntity.getUnfinishedCounts());
        }

        // 将 Map 中的值转换为 List 并返回
        return new ArrayList<>(yearDataEntitiesMap.values());
    }




}
