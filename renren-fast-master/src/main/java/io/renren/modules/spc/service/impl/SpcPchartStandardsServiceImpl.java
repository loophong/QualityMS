package io.renren.modules.spc.service.impl;

import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
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

import io.renren.modules.spc.dao.SpcPchartStandardsDao;
import io.renren.modules.spc.entity.SpcPchartStandardsEntity;
import io.renren.modules.spc.service.SpcPchartStandardsService;

import javax.annotation.Resource;


@Service("spcPchartStandardsService")
public class SpcPchartStandardsServiceImpl extends ServiceImpl<SpcPchartStandardsDao, SpcPchartStandardsEntity> implements SpcPchartStandardsService {

    @Resource
    private SpcPchartStandardsDao spcPchartStandardsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcPchartStandardsEntity> page = this.page(
                new Query<SpcPchartStandardsEntity>().getPage(params),
                new QueryWrapper<SpcPchartStandardsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcPchartStandardsEntity> datalist){

        spcPchartStandardsDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcPchartStandardsDao.batchInsertSpcPchartStandards(datalist);
    }


}