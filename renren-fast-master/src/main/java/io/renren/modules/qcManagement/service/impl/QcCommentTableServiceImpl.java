package io.renren.modules.qcManagement.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.qcManagement.dao.QcCommentTableDao;
import io.renren.modules.qcManagement.entity.QcCommentTableEntity;
import io.renren.modules.qcManagement.service.QcCommentTableService;


@Service("qcCommentTableService")
public class QcCommentTableServiceImpl extends ServiceImpl<QcCommentTableDao, QcCommentTableEntity> implements QcCommentTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcCommentTableEntity> page = this.page(
                new Query<QcCommentTableEntity>().getPage(params),
                new QueryWrapper<QcCommentTableEntity>()
        );

        return new PageUtils(page);
    }

}