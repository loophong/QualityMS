package io.renren.modules.spc.service.impl;

import io.renren.modules.spc.dao.SpcPtdDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.spc.dao.SpcPtdPvalueDao;
import io.renren.modules.spc.entity.SpcPtdPvalueEntity;
import io.renren.modules.spc.service.SpcPtdPvalueService;

import javax.annotation.Resource;


@Service("spcPtdPvalueService")
public class SpcPtdPvalueServiceImpl extends ServiceImpl<SpcPtdPvalueDao, SpcPtdPvalueEntity> implements SpcPtdPvalueService {

    @Resource
    private SpcPtdPvalueDao spcPtdPvalueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcPtdPvalueEntity> page = this.page(
                new Query<SpcPtdPvalueEntity>().getPage(params),
                new QueryWrapper<SpcPtdPvalueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcPtdPvalueEntity> datalist){
        spcPtdPvalueDao.deleteDataByTableName(datalist.get(0).getTableName());
        spcPtdPvalueDao.batchInsertSpcPtdPValueCharts(datalist);
    }

}