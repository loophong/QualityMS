package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.DepartmentTableDao;
import io.renren.modules.generator.entity.DepartmentTableEntity;
import io.renren.modules.generator.service.DepartmentTableService;


@Service("departmentTableService")
public class DepartmentTableServiceImpl extends ServiceImpl<DepartmentTableDao, DepartmentTableEntity> implements DepartmentTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DepartmentTableEntity> page = this.page(
                new Query<DepartmentTableEntity>().getPage(params),
                new QueryWrapper<DepartmentTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<DepartmentTableEntity> listAll() {
        return this.list();
    }

}