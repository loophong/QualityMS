package io.renren.modules.indicator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.indicator.entity.IndicatorIndicatorSummaryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorDictionaryDao;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;


@Service("indicatorDictionaryService")
public class IndicatorDictionaryServiceImpl extends ServiceImpl<IndicatorDictionaryDao, IndicatorDictionaryEntity> implements IndicatorDictionaryService {

    @Autowired
    private IndicatorDictionaryDao indicatorDictionaryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();

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
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getIndicatorName, value);
                                break;
                            case "managementDepartment":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getManagementDepartment, value);
                                break;
                            case "assessmentDepartment":
                                queryWrapper.lambda().like(IndicatorDictionaryEntity::getAssessmentDepartment, value);
                                break;
                            // 继续为其他字段添加查询条件...
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // 处理 JSON 解析异常
            }
        }

        IPage<IndicatorDictionaryEntity> page = this.page(
                new Query<IndicatorDictionaryEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryIndicatorList(Map<String, Object> params) {
        IPage<IndicatorDictionaryEntity> page = this.page(
                new Query<IndicatorDictionaryEntity>().getPage(params),
                new QueryWrapper<IndicatorDictionaryEntity>()
        );

        return new PageUtils(page);
    }

    public List<IndicatorDictionaryEntity> getDictionaryEntitiesByName(String indicatorName) {
        QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("indicator_name", indicatorName);
        return indicatorDictionaryDao.selectList(queryWrapper);
    }

}