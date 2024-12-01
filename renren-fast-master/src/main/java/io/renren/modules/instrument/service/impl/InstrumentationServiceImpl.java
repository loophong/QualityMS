package io.renren.modules.instrument.service.impl;

import com.alibaba.excel.util.StringUtils;
import io.renren.modules.publicmanagement.entity.InstrumentLedgerEntity;
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
        System.out.println("测试方法");
        String key = (String)params.get("key");
        IPage<InstrumentationEntity> page = this.page(
                new Query<InstrumentationEntity>().getPage(params),
                new QueryWrapper<InstrumentationEntity>().like(StringUtils.isNotBlank(key),"Warehouse_number", key)


                        .or().like(StringUtils.isNotBlank(key),"Warehouse_number", key)
                        .or().like(StringUtils.isNotBlank(key),"Tool_number", key)
                        .or().like(StringUtils.isNotBlank(key),"Tool_name", key)
                        .or().like(StringUtils.isNotBlank(key),"Specification_model", key)
                        .or().like(StringUtils.isNotBlank(key),"Range_value", key)
                        .or().like(StringUtils.isNotBlank(key),"Division_value", key)
                        .or().like(StringUtils.isNotBlank(key),"Warehouse_number", key)
                        .or().like(StringUtils.isNotBlank(key),"Accuracy", key)
                        .or().like(StringUtils.isNotBlank(key),"Purpose", key)


        );
        return new PageUtils(page);
    }


}