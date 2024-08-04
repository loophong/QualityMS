package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.generator.dao.IssueMaskTableDao;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.service.IssueMaskTableService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("issueMaskTableService")
public class IssueMaskTableServiceImpl extends ServiceImpl<IssueMaskTableDao, IssueMaskTableEntity> implements IssueMaskTableService {

    @Override
    public PageUtils recipientsqueryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        System.out.println("当前登录人信息"+role);
        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                new QueryWrapper<IssueMaskTableEntity>().eq("recipients", rolename)
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils creatorqueryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        String issueNumber = (String) params.get("issueNumber");

        QueryWrapper<IssueMaskTableEntity> queryWrapper = new QueryWrapper<IssueMaskTableEntity>()
                .eq("creator", rolename)
                .eq("issue_number", issueNumber);

        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils AuditqueryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        System.out.println("当前登录人信息"+role);
        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                new QueryWrapper<IssueMaskTableEntity>().eq("Reviewers", rolename)
        );
        return new PageUtils(page);
    }
}