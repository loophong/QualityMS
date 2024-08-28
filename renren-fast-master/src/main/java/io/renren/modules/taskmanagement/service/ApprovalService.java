package io.renren.modules.taskmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.taskmanagement.entity.ApprovalEntity;

import java.util.Map;

/**
 * 任务审批表
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-23 09:58:46
 */
public interface ApprovalService extends IService<ApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //查询我的审批列表
    PageUtils queryPageMyApprovalList(Map<String, Object> params, Long userId);

    PageUtils queryPagePendingApprovalsList(Map<String, Object> params, Long userId);

    PageUtils queryPageHistoryApprovalList(Map<String, Object> params, Long userId);

    PageUtils queryPageGetMySubmitApprovalList(Map<String, Object> params, Long userId);
}

