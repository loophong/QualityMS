package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.qcManagement.dao.QcToolsConplanDao;
import io.renren.modules.qcManagement.dao.QcToolsTemplateDao;
import io.renren.modules.qcManagement.entity.QcToolsConplanEntity;
import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import io.renren.modules.qcManagement.service.QcToolsConplanService;
import io.renren.modules.qcManagement.service.QcToolsTemplateService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("qcToolsConplanService")
public class QcToolsConplanServiceImpl extends ServiceImpl<QcToolsConplanDao, QcToolsConplanEntity> implements QcToolsConplanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcToolsConplanEntity> page = this.page(
                new Query<QcToolsConplanEntity>().getPage(params),
                new QueryWrapper<QcToolsConplanEntity>()
        );

        return new PageUtils(page);
    }
}
