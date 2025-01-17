package io.renren.modules.instrument.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.instrument.dao.InstrumentClassifyDao;
import io.renren.modules.instrument.entity.InstrumentClassifyEntity;
import io.renren.modules.instrument.service.InstrumentClassifyService;


@Service("instrumentClassifyService")
public class InstrumentClassifyServiceImpl extends ServiceImpl<InstrumentClassifyDao, InstrumentClassifyEntity> implements InstrumentClassifyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InstrumentClassifyEntity> page = this.page(
                new Query<InstrumentClassifyEntity>().getPage(params),
                new QueryWrapper<InstrumentClassifyEntity>()
        );

        return new PageUtils(page);
    }

}