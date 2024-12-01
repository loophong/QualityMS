package io.renren.modules.spc.service.impl;

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

import io.renren.modules.spc.dao.SpcPchartDao;
import io.renren.modules.spc.entity.SpcPchartEntity;
import io.renren.modules.spc.service.SpcPchartService;

import javax.annotation.Resource;


@Service("spcPchartService")
public class SpcPchartServiceImpl extends ServiceImpl<SpcPchartDao, SpcPchartEntity> implements SpcPchartService {


    @Resource
    private SpcPchartDao spcPchartDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpcPchartEntity> page = this.page(
                new Query<SpcPchartEntity>().getPage(params),
                new QueryWrapper<SpcPchartEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void importData(List<SpcPchartEntity> datalist){
        spcPchartDao.deleteDataByMonth(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        spcPchartDao.batchInsertSpcPcharts(datalist);
    }

}