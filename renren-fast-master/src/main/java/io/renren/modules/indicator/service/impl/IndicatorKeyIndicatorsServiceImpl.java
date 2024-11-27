package io.renren.modules.indicator.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Service("indicatorKeyIndicatorsService")
public class IndicatorKeyIndicatorsServiceImpl extends ServiceImpl<IndicatorKeyIndicatorsDao, IndicatorKeyIndicatorsEntity> implements IndicatorKeyIndicatorsService {
    @Autowired
    private IndicatorKeyIndicatorsDao  indicatorKeyIndicatorsDao;
    @Override
    public PageUtils queryPageFinishedList(Map<String, Object> params) {
        log.info("param"+params.get("page")+"------"+ params.get("limit"));

        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<IndicatorKeyIndicatorsEntity> page = new Page<IndicatorKeyIndicatorsEntity>(p,l);
        Page<IndicatorKeyIndicatorsEntity> result = indicatorKeyIndicatorsDao.selectFinishedSubjectList(page);
        log.info("page"+page);
        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPage1(Map<String, Object> params) {
        return null;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndicatorKeyIndicatorsEntity> page = this.page(
                new Query<IndicatorKeyIndicatorsEntity>().getPage(params),
                new QueryWrapper<IndicatorKeyIndicatorsEntity>()
        );

        return new PageUtils(page);
    }


}