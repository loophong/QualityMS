package io.renren.modules.spc.service.impl;

import io.renren.modules.spc.entity.SpcXrchartEntity;
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

import io.renren.modules.spc.dao.SpcXrchartStandardsDao;
import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
import io.renren.modules.spc.service.SpcXrchartStandardsService;

import javax.annotation.Resource;


@Service("spcXrchartStandardsService")
public class SpcXrchartStandardsServiceImpl extends ServiceImpl<SpcXrchartStandardsDao, SpcXrchartStandardsEntity> implements SpcXrchartStandardsService {

    @Resource
    private SpcXrchartStandardsDao spcXrchartStandardsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcXrchartStandardsEntity> page = this.page(
                new Query<SpcXrchartStandardsEntity>().getPage(params),
                new QueryWrapper<SpcXrchartStandardsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcXrchartStandardsEntity> datalist){

//        spcXrchartStandardsDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcXrchartStandardsDao.deleteDataByTableName(datalist.get(0).getAlternateFields1());
        spcXrchartStandardsDao.batchInsertSpcXrchartStandards(datalist);
    }

}