package io.renren.modules.qcManagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.qcManagement.dao.QcStepDao;
import io.renren.modules.qcManagement.entity.QcStepEntity;
import io.renren.modules.qcManagement.service.QcStepService;


@Service("qcStepService")
public class QcStepServiceImpl extends ServiceImpl<QcStepDao, QcStepEntity> implements QcStepService {

    @Override
    public PageUtils getListById(Map<String, Object> params) {
        IPage<QcStepEntity> page = this.page(
                new Query<QcStepEntity>().getPage(params),
                new QueryWrapper<QcStepEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcStepEntity> page = this.page(
                new Query<QcStepEntity>().getPage(params),
                new QueryWrapper<QcStepEntity>()
        );

        return new PageUtils(page);
    }

}
