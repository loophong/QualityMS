package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.TestTableDao;
import io.renren.modules.generator.entity.TestTableEntity;
import io.renren.modules.generator.service.TestTableService;


@Service("testTableService")
public class TestTableServiceImpl extends ServiceImpl<TestTableDao, TestTableEntity> implements TestTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TestTableEntity> page = this.page(
                new Query<TestTableEntity>().getPage(params),
                new QueryWrapper<TestTableEntity>()
        );

        return new PageUtils(page);
    }

}