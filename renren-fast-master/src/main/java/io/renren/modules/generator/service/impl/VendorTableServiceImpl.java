package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.VendorTableDao;
import io.renren.modules.generator.entity.VendorTableEntity;
import io.renren.modules.generator.service.VendorTableService;


@Service("vendorTableService")
public class VendorTableServiceImpl extends ServiceImpl<VendorTableDao, VendorTableEntity> implements VendorTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VendorTableEntity> page = this.page(
                new Query<VendorTableEntity>().getPage(params),
                new QueryWrapper<VendorTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<VendorTableEntity> listAll() {
        return this.list();
    }

}