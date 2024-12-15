package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.VerificationDurationTableDao;
import io.renren.modules.generator.entity.VerificationDurationTableEntity;
import io.renren.modules.generator.service.VerificationDurationTableService;


@Service("verificationDurationTableService")
public class VerificationDurationTableServiceImpl extends ServiceImpl<VerificationDurationTableDao, VerificationDurationTableEntity> implements VerificationDurationTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VerificationDurationTableEntity> page = this.page(
                new Query<VerificationDurationTableEntity>().getPage(params),
                new QueryWrapper<VerificationDurationTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public VerificationDurationTableEntity getLatestRecord() {
        // 使用 LambdaQueryWrapper 按 ID 倒序排列，取第一条记录
        return this.getOne(new LambdaQueryWrapper<VerificationDurationTableEntity>()
                .orderByDesc(VerificationDurationTableEntity::getExamineId)
                .last("LIMIT 1"));
    }

}