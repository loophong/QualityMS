package io.renren.modules.qcManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.qcManagement.dao.QcGroupMemberDao;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.service.QcGroupMemberService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("qcGroupMemberService")
public class QcGroupMemberServiceImpl extends ServiceImpl<QcGroupMemberDao, QcGroupMemberEntity> implements QcGroupMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QcGroupMemberEntity> page = this.page(
                new Query<QcGroupMemberEntity>().getPage(params),
                new QueryWrapper<QcGroupMemberEntity>()
        );

        return new PageUtils(page);
    }

}
