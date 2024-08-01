package io.renren.modules.indicator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.indicator.dao.IndicatorDictionaryDao;
import io.renren.modules.indicator.entity.IndicatorDictionaryEntity;
import io.renren.modules.indicator.service.IndicatorDictionaryService;


@Service("indicatorDictionaryService")
public class IndicatorDictionaryServiceImpl extends ServiceImpl<IndicatorDictionaryDao, IndicatorDictionaryEntity> implements IndicatorDictionaryService {

    @Autowired
    private IndicatorDictionaryDao indicatorDictionaryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndicatorDictionaryEntity> page = this.page(
                new Query<IndicatorDictionaryEntity>().getPage(params),
                new QueryWrapper<IndicatorDictionaryEntity>()
        );

        return new PageUtils(page);
    }

    public List<IndicatorDictionaryEntity> getDictionaryEntitiesByName(String indicatorName) {
        QueryWrapper<IndicatorDictionaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("indicator_name", indicatorName);
        return indicatorDictionaryDao.selectList(queryWrapper);
    }

}