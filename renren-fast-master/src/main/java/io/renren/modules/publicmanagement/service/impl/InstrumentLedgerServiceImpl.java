package io.renren.modules.publicmanagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.publicmanagement.dao.InstrumentLedgerDao;
import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;
import io.renren.modules.publicmanagement.service.InstrumentLedgerService;


@Service("instrumentLedgerService")
public class InstrumentLedgerServiceImpl extends ServiceImpl<InstrumentLedgerDao, InstrumentLedgerEntity> implements InstrumentLedgerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InstrumentLedgerEntity> page = this.page(
                new Query<InstrumentLedgerEntity>().getPage(params),
                new QueryWrapper<InstrumentLedgerEntity>()
        );

        return new PageUtils(page);
    }

}