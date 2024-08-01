package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.generator.dao.IssueCarTypeTableDao;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;
import io.renren.modules.generator.service.IssueCarTypeTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("issueCarTypeTableService")
public class IssueCarTypeTableServiceImpl extends ServiceImpl<IssueCarTypeTableDao, IssueCarTypeTableEntity> implements IssueCarTypeTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IssueCarTypeTableEntity> page = this.page(
                new Query<IssueCarTypeTableEntity>().getPage(params),
                new QueryWrapper<IssueCarTypeTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<IssueCarTypeTableEntity> listAll() {
        return this.list();
    }

}