package io.renren.modules.qcManagement.service.impl;

import io.renren.common.utils.R;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.qcManagement.dao.QcAllCountDao;
import io.renren.modules.qcManagement.entity.QcAllCountEntity;
import io.renren.modules.qcManagement.service.QcAllCountService;


@Service("qcAllCountService")
public class QcAllCountServiceImpl extends ServiceImpl<QcAllCountDao, QcAllCountEntity> implements QcAllCountService {


    @Autowired
    private QcAllCountDao qcAllCountDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcAllCountEntity> page = this.page(
                new Query<QcAllCountEntity>().getPage(params),
                new QueryWrapper<QcAllCountEntity>()
        );

        return new PageUtils(page);
    }

}
