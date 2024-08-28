package io.renren.modules.indicator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorDataDictionaryDao;
import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDataDictionaryService;


@Service("indicatorDataDictionaryService")
public class IndicatorDataDictionaryServiceImpl extends ServiceImpl<IndicatorDataDictionaryDao, IndicatorDataDictionaryEntity> implements IndicatorDataDictionaryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        System.out.println("params:" + params);
        String key = (String) params.get("key");
        QueryWrapper<IndicatorDataDictionaryEntity> queryWrapper = new QueryWrapper<>();

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
                            case "dataContent":
                                queryWrapper.lambda().like(IndicatorDataDictionaryEntity::getDataContent, value);
                                break;
                            case "dataType":
                                queryWrapper.lambda().like(IndicatorDataDictionaryEntity::getDataType, value);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        IPage<IndicatorDataDictionaryEntity> page = this.page(
                new Query<IndicatorDataDictionaryEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}