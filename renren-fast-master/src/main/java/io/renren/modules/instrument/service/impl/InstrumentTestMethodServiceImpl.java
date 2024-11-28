package io.renren.modules.instrument.service.impl;

import com.alibaba.excel.util.StringUtils;
import io.renren.modules.instrument.entity.InstrumentationEntity;
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
        System.out.println("测试方法");
        String key = (String)params.get("key");
        IPage<InstrumentTestMethodEntity> page = this.page(
                new Query<InstrumentTestMethodEntity>().getPage(params),
                new QueryWrapper<InstrumentTestMethodEntity>().like(StringUtils.isNotBlank(key),"Department", key)


                        .or().like(StringUtils.isNotBlank(key),"StandardNumber", key)
                        .or().like(StringUtils.isNotBlank(key),"Compilation", key)
                        .or().like(StringUtils.isNotBlank(key),"CompilationDate", key)
                        .or().like(StringUtils.isNotBlank(key),"Reviewer", key)
                        .or().like(StringUtils.isNotBlank(key),"ReviewDate", key)
                        .or().like(StringUtils.isNotBlank(key),"Creat_people", key)
                        .or().like(StringUtils.isNotBlank(key),"CalibrationTool", key)
                        .or().like(StringUtils.isNotBlank(key),"InspectionFrequency", key)
                        .or().like(StringUtils.isNotBlank(key),"MeasurementInstrument", key)


        );
        return new PageUtils(page);
    }

}