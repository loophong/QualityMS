package io.renren.modules.indicator.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorKeyIndicatorsDao;
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.indicator.service.IndicatorKeyIndicatorsService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;


@Slf4j
@Service("indicatorKeyIndicatorsService")
public class IndicatorKeyIndicatorsServiceImpl extends ServiceImpl<IndicatorKeyIndicatorsDao, IndicatorKeyIndicatorsEntity> implements IndicatorKeyIndicatorsService {
    @Autowired
    IndicatorKeyIndicatorsDao indicatorKeyIndicatorsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorKeyIndicatorsEntity> queryWrapper = new QueryWrapper<>();

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
                            case "indicatorId":
                                Integer indicatorId = Integer.parseInt(value);
                                queryWrapper.lambda().eq(IndicatorKeyIndicatorsEntity::getIndicatorId, indicatorId);
                                break;
                            case "indicatorName":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getIndicatorName, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getSourceDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getAssessmentDepartment, value);
                                break;
                            case "indicatorClassification":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getIndicatorClassification, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<IndicatorKeyIndicatorsEntity> list = this.list(queryWrapper);
        // 使用流操作去重，确保 indicatorId 是唯一的
        List<IndicatorKeyIndicatorsEntity> distinctList = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                IndicatorKeyIndicatorsEntity::getIndicatorId,
                                entity -> entity,
                                (existing, replacement) -> existing
                        ),
                        map -> new ArrayList<>(map.values())
                ));

        int totalCount = distinctList.size();

        IPage<IndicatorKeyIndicatorsEntity> page = this.page(
                new Query<IndicatorKeyIndicatorsEntity>().getPage(params),
                queryWrapper
        );

        // 设置分组后的 totalCount 到 PageUtils
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(totalCount);

        return pageUtils;
    }

    //    @Override
//    public PageUtils queryPageFinishedList(Map<String, Object> params) {
//        log.info("param"+params.get("page")+"------"+ params.get("limit"));
//
//        long p = Long.parseLong((String) params.get("page"));
//        long l = Long.parseLong((String) params.get("limit"));
//        Page<IndicatorKeyIndicatorsEntity> page = new Page<IndicatorKeyIndicatorsEntity>(p,l);
//
//        Page<IndicatorKeyIndicatorsEntity> result = indicatorKeyIndicatorsDao.selectFinishedSubjectList(page);
//        log.info("page"+page);
//        return new PageUtils(page);
//    }
//}
    @Override
    public PageUtils queryPage1(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorKeyIndicatorsEntity> queryWrapper = new QueryWrapper<>();

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
                            case "indicatorId":
                                Integer indicatorId = Integer.parseInt(value);
                                queryWrapper.lambda().eq(IndicatorKeyIndicatorsEntity::getIndicatorId, indicatorId);
                                break;
                            case "indicatorName":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getIndicatorName, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getSourceDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getAssessmentDepartment, value);
                                break;
                            case "indicatorClassification":
                                queryWrapper.lambda().like(IndicatorKeyIndicatorsEntity::getIndicatorClassification, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        queryWrapper.lambda().eq(IndicatorKeyIndicatorsEntity::getStorageFlag, 1);
        List<IndicatorKeyIndicatorsEntity> list = this.list(queryWrapper);
        // 使用流操作去重，确保 indicatorId 是唯一的
        List<IndicatorKeyIndicatorsEntity> distinctList = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                IndicatorKeyIndicatorsEntity::getIndicatorId,
                                entity -> entity,
                                (existing, replacement) -> existing
                        ),
                        map -> new ArrayList<>(map.values())
                ));

        int totalCount = distinctList.size();

        IPage<IndicatorKeyIndicatorsEntity> page = this.page(
                new Query<IndicatorKeyIndicatorsEntity>().getPage(params),
                queryWrapper
        );

        // 设置分组后的 totalCount 到 PageUtils
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(totalCount);

        return pageUtils;
    }
    public List<IndicatorKeyIndicatorsEntity> queryList(Map<String, Object> params) {
        QueryWrapper<IndicatorKeyIndicatorsEntity> queryWrapper = new QueryWrapper<>();
        System.out.println("params:" + params);

        // 只保留这一条查询语句
        queryWrapper.lambda().eq(IndicatorKeyIndicatorsEntity::getStorageFlag, 1);

        // 返回查询结果列表
        return list(queryWrapper);
    }
    @Override
    public boolean clearStorageFlag(List<Integer> ids) {
        List<IndicatorKeyIndicatorsEntity> list = this.list(new LambdaQueryWrapper<IndicatorKeyIndicatorsEntity>().in(IndicatorKeyIndicatorsEntity::getKeyIndicatorId, ids));
        for (IndicatorKeyIndicatorsEntity indicatorKeyIndicatorsEntity : list) {
            indicatorKeyIndicatorsEntity.setStorageFlag(0);
        }
        this.saveOrUpdateBatch(list);
        return true;
    }
}
