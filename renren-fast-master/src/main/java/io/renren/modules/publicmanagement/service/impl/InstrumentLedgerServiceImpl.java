package io.renren.modules.publicmanagement.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.modules.indicator.entity.IndicatorDataDictionaryEntity;
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
            String key = (String)params.get("key");
            IPage<InstrumentLedgerEntity> page = this.page(
                    new Query<InstrumentLedgerEntity>().getPage(params),
                    new QueryWrapper<InstrumentLedgerEntity>().like(StringUtils.isNotBlank(key),"company_id", key)

                     .or().like(StringUtils.isNotBlank(key),"level", key)
                            .or().like(StringUtils.isNotBlank(key),"calibration_cycle", key)
                     .or().like(StringUtils.isNotBlank(key),"calibration_status", key)

            );
            return new PageUtils(page);
        }



}