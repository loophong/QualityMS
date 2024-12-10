package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.generator.dao.IssueMaskTableDao;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueMaskTableService;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("issueMaskTableService")
public class IssueMaskTableServiceImpl extends ServiceImpl<IssueMaskTableDao, IssueMaskTableEntity> implements IssueMaskTableService {

    @Autowired
    private IssueTableService issueTableService;

    @Override
    public PageUtils recipientsqueryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
//        System.out.println("当前登录人信息"+role);
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
    public PageUtils managerqueryPage(Map<String, Object> params) {
        String issueNumber = (String) params.get("issueNumber");

        QueryWrapper<IssueMaskTableEntity> queryWrapper = new QueryWrapper<IssueMaskTableEntity>()
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
//        System.out.println("当前登录人信息"+role);
        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                new QueryWrapper<IssueMaskTableEntity>()
                        .eq("Reviewers", rolename) // 筛选Reviewers为当前登录用户
                        .eq("state", "审核中") // 筛选state为“审核中”的数据
        );
        return new PageUtils(page);
    }

    @Override
    public String newIssuemaskNumber(String issueNumber) {
        //查询相同问题编号的问题
        List<IssueMaskTableEntity> list1 = this.list(
                new QueryWrapper<IssueMaskTableEntity>()
                       .eq("issue_number", issueNumber)
        );

        // 如果数据库为空，返回默认值 "0001"
        if (list1.isEmpty()) {
            return "0001";
        }
        //如果查询到相同问题编号的问题，则获取最大的maskNumber，并加1
//        // 找到 ID 最大的问题
//        IssueMaskTableEntity maxIssue = list1.stream()
//                .max(Comparator.comparingLong(IssueMaskTableEntity::getIssuemaskId)) // 根据 ID 获取最大值
//                .orElse(null); // 如果没有记录，返回 null
        // 如果查询到相同问题编号的问题，找到后四位数字最大的记录
        IssueMaskTableEntity maxIssue = list1.stream()
                .max(Comparator.comparingInt(issue -> {
                    String serialNumber = issue.getSerialNumber();
                    if (serialNumber != null && serialNumber.length() >= 4) {
                        // 提取后四位数字并转为整数
                        return Integer.parseInt(serialNumber.substring(serialNumber.length() - 4));
                    }
                    return 0; // 如果后四位无效，返回 0 作为默认值
                }))
                .orElse(null);
        // 处理问题编号，取后四位并加1
        if (maxIssue != null) {
            // 获取最大问题的编号
            String currentIssueNumber = maxIssue.getSerialNumber(); // 假设有一个方法 getIssueNumber()
            System.out.println("当前最大问题编号为：" + currentIssueNumber);
            // 处理问题编号，取后四位并加1
            String lastFourDigits = currentIssueNumber.substring(currentIssueNumber.length() - 4);
            int newNumber = Integer.parseInt(lastFourDigits) + 1;

            // 返回格式化的编号（例如补零）
            return String.format("%04d", newNumber); // 修改这里返回字符串
        }

        return "0001";
    }

    @Override
    public String statechange(String issueNumber) {
        // 查询相同问题编号的任务
        List<IssueMaskTableEntity> list1 = this.list(
                new QueryWrapper<IssueMaskTableEntity>()
                        .eq("issue_number", issueNumber)
        );

        // 检查是否所有任务的state都为"已完成"
        boolean allCompleted = list1.stream()
                .allMatch(task -> "已完成".equals(task.getState())); // 使用 stream 检查是否所有任务的 state 为 "已完成"

        // 如果全部为"已完成"，更新 IssueTable 中的状态
        if (allCompleted) {
            // 更新 IssueTable 中的对应记录
            IssueTableEntity issue = new IssueTableEntity();
            issue.setLevel("等待验证指定"); // 设置新状态

            // 使用 update 方法更新 IssueTable 中的状态
            boolean rowsUpdated = issueTableService.update(
                    issue,
                    new QueryWrapper<IssueTableEntity>().eq("issue_number", issueNumber)
            );

            // 如果更新成功，返回相应的信息
            if (rowsUpdated) {
                return "状态更新成功";
            } else {
                return "状态更新失败";
            }
        }

        // 如果没有全部为"已完成"，返回对应的提示
        return "部分任务尚未完成，状态未更新";
    }



    @Override
    public List<IssueMaskTableEntity> listAll(String issueNumber) {
        List<IssueMaskTableEntity> list1 = this.list();
        return list1.stream()
//                .filter(issue -> issue.getIssueNumber().equals(issueNumber))
                .filter(issue -> issue.getIssueNumber().equals(issueNumber) && !"已派发".equals(issue.getState()) && !"未通过审核".equals(issue.getState()))
                .collect(Collectors.toList());
    }

    @Override
    public String listRecords(String issueNumber) {
        List<IssueMaskTableEntity> list1 = this.list();
        boolean allCompleted = list1.stream()
                .filter(issue -> issue.getIssueNumber().equals(issueNumber))
                .allMatch(issue -> "已完成".equals(issue.getState()) || "已派发".equals(issue.getState()) || "未通过审核".equals(issue.getState()) );

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
//                System.out.println("派发任务前置数节点修改成功++++++++++++++++++");
                this.updateById(task); // 更新数据库中的记录
            }
        }
        // 遍历所有任务，将所有serialNumber为传入的issueNumber的任务的serialNumber改为传入的newSerialNumber（将现任务的上级任务改为原任务的上级任务）
        for (IssueMaskTableEntity task : list1) {
            if (task.getSuperiorMask() != null && task.getSuperiorMask().equals(issueNumber)) {
                task.setSuperiorMask(serialNumber);
                this.updateById(task); // 更新数据库中的记录
//                System.out.println("派发任务后续子树节点修改成功++++++++++++++++++");
            }
        }
        return null;
    }

    @Override
    public PageUtils acceptqueryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        System.out.println("当前登录人信息"+role);
        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                new QueryWrapper<IssueMaskTableEntity>().eq("recipients", rolename)
                        .ne("state", "已派发") // 筛选state不为“已派发”的数据
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils distributequeryPage(Map<String, Object> params) {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        System.out.println("当前登录人信息"+role);
        IPage<IssueMaskTableEntity> page = this.page(
                new Query<IssueMaskTableEntity>().getPage(params),
                new QueryWrapper<IssueMaskTableEntity>().eq("recipients", rolename)
                        .eq("state", "已派发") // 筛选state为“已派发”的数据
        );
        return new PageUtils(page);
    }

}