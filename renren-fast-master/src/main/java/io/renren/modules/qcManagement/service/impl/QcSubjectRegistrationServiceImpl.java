package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.qcManagement.dao.QcSubjectRegistrationDao;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.service.QcSubjectRegistrationService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("qcSubjectRegistrationService")
public class QcSubjectRegistrationServiceImpl extends ServiceImpl<QcSubjectRegistrationDao, QcSubjectRegistrationEntity> implements QcSubjectRegistrationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcSubjectRegistrationEntity> page = this.page(
                new Query<QcSubjectRegistrationEntity>().getPage(params),
                new QueryWrapper<QcSubjectRegistrationEntity>()
        );

        return new PageUtils(page);
    }

}
