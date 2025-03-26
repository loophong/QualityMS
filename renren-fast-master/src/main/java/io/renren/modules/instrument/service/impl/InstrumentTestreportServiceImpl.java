package io.renren.modules.instrument.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.instrument.dao.InstrumentTestreportDao;
import io.renren.modules.instrument.entity.InstrumentTestreportEntity;
import io.renren.modules.instrument.service.InstrumentTestreportService;


@Service("instrumentTestreportService")
public class InstrumentTestreportServiceImpl extends ServiceImpl<InstrumentTestreportDao, InstrumentTestreportEntity> implements InstrumentTestreportService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InstrumentTestreportEntity> page = this.page(
                new Query<InstrumentTestreportEntity>().getPage(params),
                new QueryWrapper<InstrumentTestreportEntity>()
        );

        return new PageUtils(page);
    }

}