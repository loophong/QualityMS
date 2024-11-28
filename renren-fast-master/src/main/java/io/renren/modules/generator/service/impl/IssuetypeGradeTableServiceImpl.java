package io.renren.modules.generator.service.impl;

import io.renren.modules.generator.entity.IssueTypeTableEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.IssuetypeGradeTableDao;
import io.renren.modules.generator.entity.IssuetypeGradeTableEntity;
import io.renren.modules.generator.service.IssuetypeGradeTableService;


@Service("issuetypeGradeTableService")
public class IssuetypeGradeTableServiceImpl extends ServiceImpl<IssuetypeGradeTableDao, IssuetypeGradeTableEntity> implements IssuetypeGradeTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IssuetypeGradeTableEntity> page = this.page(
                new Query<IssuetypeGradeTableEntity>().getPage(params),
                new QueryWrapper<IssuetypeGradeTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<IssuetypeGradeTableEntity> listAll() {
        return this.list();
    }

}