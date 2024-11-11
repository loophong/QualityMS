package io.renren.modules.instrument.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.instrument.dao.InstrumentationDao;
import io.renren.modules.instrument.entity.InstrumentationEntity;
import io.renren.modules.instrument.service.InstrumentationService;


@Service("instrumentationService")
public class InstrumentationServiceImpl extends ServiceImpl<InstrumentationDao, InstrumentationEntity> implements InstrumentationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InstrumentationEntity> page = this.page(
                new Query<InstrumentationEntity>().getPage(params),
                new QueryWrapper<InstrumentationEntity>()
        );

        return new PageUtils(page);
    }

}