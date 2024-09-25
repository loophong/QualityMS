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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
                new QueryWrapper<IssueMaskTableEntity>()
                        .eq("Reviewers", rolename) // 筛选Reviewers为当前登录用户
                        .eq("state", "审核中") // 筛选state为“审核中”的数据
        );
        return new PageUtils(page);
    }


    @Override
    public List<IssueMaskTableEntity> listAll(String issueNumber) {
        List<IssueMaskTableEntity> list1 = this.list();
        return list1.stream()
//                .filter(issue -> issue.getIssueNumber().equals(issueNumber))
                .filter(issue -> issue.getIssueNumber().equals(issueNumber) && !"已派发".equals(issue.getState()))
                .collect(Collectors.toList());
    }

    @Override
    public String listRecords(String issueNumber) {
        List<IssueMaskTableEntity> list1 = this.list();
        boolean allCompleted = list1.stream()
                .filter(issue -> issue.getIssueNumber().equals(issueNumber))
                .allMatch(issue -> "已完成".equals(issue.getState()) || "已派发".equals(issue.getState()));

        if (allCompleted) {
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String dispatch(String issueNumber , String serialNumber) {
        List<IssueMaskTableEntity> list1 = this.list();
        // 遍历所有任务，将其中issueNumber为传入的issueNumber的serialNumber修改为null（原任务的上级任务改为1，原问题状态改为已派发）
        for (IssueMaskTableEntity task : list1) {
            if (task.getSerialNumber().equals(issueNumber)) {
                task.setSuperiorMask(String.valueOf(1));
                task.setState("已派发");
                System.out.println("派发任务前置数节点修改成功++++++++++++++++++");
                this.updateById(task); // 更新数据库中的记录
            }
        }
        // 遍历所有任务，将所有serialNumber为传入的issueNumber的任务的serialNumber改为传入的newSerialNumber（将现任务的上级任务改为原任务的上级任务）
        for (IssueMaskTableEntity task : list1) {
            if (task.getSuperiorMask() != null && task.getSuperiorMask().equals(issueNumber)) {
                task.setSuperiorMask(serialNumber);
                this.updateById(task); // 更新数据库中的记录
                System.out.println("派发任务后续子树节点修改成功++++++++++++++++++");
            }
        }
        return null;
    }

}