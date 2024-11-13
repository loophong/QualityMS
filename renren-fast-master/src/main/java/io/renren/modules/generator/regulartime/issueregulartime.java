package io.renren.modules.generator.regulartime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component

public class issueregulartime {

    @Autowired
    private IssueTableService issueTableService;
    @Scheduled(cron = "0 0 9,17 * * *")
    public void taskScheduledTasks() {
        log.info("定时任务执行了");
        checkIssuesrecurrence();
    }
    /**
     * @description: 检查任务是否复发
     * @author: hong
     * @date: 2024/8/27 13:38
     * @version: 1.0
     */
    public void checkIssuesrecurrence() {
        // 创建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("over_due", true);

        // 查询符合条件的记录
        queryWrapper.eq("over_due", true);

        // 获取当前日期
        Date currentDate = new Date();

        List<IssueTableEntity> overdueIssues = issueTableService.list(queryWrapper);
        // 判断 verification_deadline 是否为空
        // 遍历查询结果，判断 verification_deadline 是否为空，并对比截止日期
        for (IssueTableEntity issue : overdueIssues) {
            Date verificationDeadline = issue.getVerificationDeadline();
            if (verificationDeadline == null) {
                log.info("检测到过期任务，但未设置验证截止日期，任务ID: " + issue.getIssueId());
                // 根据业务需求进一步处理，比如发出警告或通知
            } else {
                log.info("过期任务的验证截止日期: " + verificationDeadline);
                // 比较当前日期与验证截止日期
                if (currentDate.before(verificationDeadline)) {
                    // 当前日期在截止日期之前，执行下一步操作
                    log.info("当前日期在验证截止日期之前，进行下一步操作，任务ID: " + issue.getIssueId());
                    // 在此添加进一步操作的代码
                } else {
                    // 当前日期已超过验证截止日期，不执行操作
                    log.info("当前日期已超过验证截止日期，无需进一步操作，任务ID: " + issue.getIssueId());
                }
            }
        }

    }
}
