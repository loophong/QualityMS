package io.renren.modules.indicator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorTableDao;
import io.renren.modules.indicator.entity.IndicatorTableEntity;
import io.renren.modules.indicator.service.IndicatorTableService;


@Service("indicatorTableService")
public class IndicatorTableServiceImpl extends ServiceImpl<IndicatorTableDao, IndicatorTableEntity> implements IndicatorTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndicatorTableEntity> page = this.page(
                new Query<IndicatorTableEntity>().getPage(params),
                new QueryWrapper<IndicatorTableEntity>()
        );

        return new PageUtils(page);
    }

}