package io.renren.modules.indicator.service.impl;

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
        IPage<IndicatorDataDictionaryEntity> page = this.page(
                new Query<IndicatorDataDictionaryEntity>().getPage(params),
                new QueryWrapper<IndicatorDataDictionaryEntity>()
        );

        return new PageUtils(page);
    }

}