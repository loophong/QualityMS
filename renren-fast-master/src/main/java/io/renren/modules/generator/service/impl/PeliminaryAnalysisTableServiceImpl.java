package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.PeliminaryAnalysisTableDao;
import io.renren.modules.generator.entity.PeliminaryAnalysisTableEntity;
import io.renren.modules.generator.service.PeliminaryAnalysisTableService;


@Service("peliminaryAnalysisTableService")
public class PeliminaryAnalysisTableServiceImpl extends ServiceImpl<PeliminaryAnalysisTableDao, PeliminaryAnalysisTableEntity> implements PeliminaryAnalysisTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PeliminaryAnalysisTableEntity> page = this.page(
                new Query<PeliminaryAnalysisTableEntity>().getPage(params),
                new QueryWrapper<PeliminaryAnalysisTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PeliminaryAnalysisTableEntity> listAll() {
        return this.list();
    }

}