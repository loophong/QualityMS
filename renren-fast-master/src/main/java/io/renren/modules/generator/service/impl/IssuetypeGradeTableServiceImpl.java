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
        // 构建查询条件
        QueryWrapper<IssuetypeGradeTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 ID 降序排序
        queryWrapper.orderByDesc("grade_ID");

        // 分页查询
        IPage<IssuetypeGradeTableEntity> page = this.page(
                new Query<IssuetypeGradeTableEntity>().getPage(params),
                queryWrapper
        );

        // 返回分页数据
        return new PageUtils(page);
    }


    @Override
    public List<IssuetypeGradeTableEntity> listAll() {
        return this.list();
    }

}