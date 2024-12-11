package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.generator.dao.IssueTypeTableDao;
import io.renren.modules.generator.entity.IssueTypeTableEntity;
import io.renren.modules.generator.service.IssueTypeTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("issueTypeTableService")
public class IssueTypeTableServiceImpl extends ServiceImpl<IssueTypeTableDao, IssueTypeTableEntity> implements IssueTypeTableService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<IssueTypeTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 ID 降序排序
        queryWrapper.orderByDesc("issue_category_id");

        // 分页查询
        IPage<IssueTypeTableEntity> page = this.page(
                new Query<IssueTypeTableEntity>().getPage(params),
                queryWrapper
        );

        // 返回分页数据
        return new PageUtils(page);
    }


    @Override
    public List<IssueTypeTableEntity> listAll() {
        return this.list();
    }

}