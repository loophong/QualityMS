package io.renren.modules.indicator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorKeyIndicatorsDao;
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.indicator.service.IndicatorKeyIndicatorsService;


@Service("indicatorKeyIndicatorsService")
public class IndicatorKeyIndicatorsServiceImpl extends ServiceImpl<IndicatorKeyIndicatorsDao, IndicatorKeyIndicatorsEntity> implements IndicatorKeyIndicatorsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndicatorKeyIndicatorsEntity> page = this.page(
                new Query<IndicatorKeyIndicatorsEntity>().getPage(params),
                new QueryWrapper<IndicatorKeyIndicatorsEntity>()
        );

        return new PageUtils(page);
    }


}