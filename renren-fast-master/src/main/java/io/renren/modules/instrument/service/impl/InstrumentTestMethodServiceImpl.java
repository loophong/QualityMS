package io.renren.modules.instrument.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.instrument.dao.InstrumentTestMethodDao;
import io.renren.modules.instrument.entity.InstrumentTestMethodEntity;
import io.renren.modules.instrument.service.InstrumentTestMethodService;


@Service("instrumentTestMethodService")
public class InstrumentTestMethodServiceImpl extends ServiceImpl<InstrumentTestMethodDao, InstrumentTestMethodEntity> implements InstrumentTestMethodService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InstrumentTestMethodEntity> page = this.page(
                new Query<InstrumentTestMethodEntity>().getPage(params),
                new QueryWrapper<InstrumentTestMethodEntity>()
        );

        return new PageUtils(page);
    }

}