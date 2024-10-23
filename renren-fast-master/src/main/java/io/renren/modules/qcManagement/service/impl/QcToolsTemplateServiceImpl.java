package io.renren.modules.qcManagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.qcManagement.dao.QcToolsTemplateDao;
import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import io.renren.modules.qcManagement.service.QcToolsTemplateService;


@Service("qcToolsTemplateService")
public class QcToolsTemplateServiceImpl extends ServiceImpl<QcToolsTemplateDao, QcToolsTemplateEntity> implements QcToolsTemplateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcToolsTemplateEntity> page = this.page(
                new Query<QcToolsTemplateEntity>().getPage(params),
                new QueryWrapper<QcToolsTemplateEntity>()
        );

        return new PageUtils(page);
    }

}
