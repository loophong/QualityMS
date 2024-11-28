package io.renren.modules.spc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.renren.modules.spc.dao.SpcXrchartDao;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.service.SpcXrchartService;

import javax.annotation.Resource;


@Service("spcXrchartService")
public class SpcXrchartServiceImpl extends ServiceImpl<SpcXrchartDao, SpcXrchartEntity> implements SpcXrchartService {


    @Resource
    private SpcXrchartDao spcXrchartDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcXrchartEntity> page = this.page(
                new Query<SpcXrchartEntity>().getPage(params),
                new QueryWrapper<SpcXrchartEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcXrchartEntity> datalist){

        spcXrchartDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcXrchartDao.batchInsertSpcXrcharts(datalist);
    }

}