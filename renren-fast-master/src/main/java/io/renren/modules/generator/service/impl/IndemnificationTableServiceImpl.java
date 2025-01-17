package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.IndemnificationTableDao;
import io.renren.modules.generator.entity.IndemnificationTableEntity;
import io.renren.modules.generator.service.IndemnificationTableService;


@Service("indemnificationTableService")
public class IndemnificationTableServiceImpl extends ServiceImpl<IndemnificationTableDao, IndemnificationTableEntity> implements IndemnificationTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IndemnificationTableEntity> page = this.page(
                new Query<IndemnificationTableEntity>().getPage(params),
                new QueryWrapper<IndemnificationTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<IndemnificationTableEntity> getAllIndemnifications() {
        return this.list();
    }

    @Override
    public IndemnificationTableEntity getIndemnificationById(int id) {
        return this.getOne(new QueryWrapper<IndemnificationTableEntity>().eq("Inde_id", id));
    }

}