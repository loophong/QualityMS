package io.renren.modules.indicator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.generator.dao.IssueTableDao;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorKeyIndicatorsDao;
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.indicator.service.IndicatorKeyIndicatorsService;

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

        IPage<IndicatorKeyIndicatorsEntity> page = this.page(
                new Query<IndicatorKeyIndicatorsEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageFinishedList(Map<String, Object> params) {
        log.info("param"+params.get("page")+"------"+ params.get("limit"));

        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<IndicatorKeyIndicatorsEntity> page = new Page<IndicatorKeyIndicatorsEntity>(p,l);

        Page<IndicatorKeyIndicatorsEntity> result = indicatorKeyIndicatorsDao.selectFinishedSubjectList(page);
        log.info("page"+page);
        return new PageUtils(page);
    }



}