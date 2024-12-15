package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.taskmanagement.dao.ApprovalFileDao;
import io.renren.modules.taskmanagement.entity.ApprovalFileEntity;
import io.renren.modules.taskmanagement.service.ApprovalFileService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("approvalFileService")
public class ApprovalFileServiceImpl extends ServiceImpl<ApprovalFileDao, ApprovalFileEntity> implements ApprovalFileService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ApprovalFileEntity> page = this.page(
                new Query<ApprovalFileEntity>().getPage(params),
                new QueryWrapper<ApprovalFileEntity>()
        );

        return new PageUtils(page);
    }

}