package io.renren.modules.notice.service.impl;

import io.renren.modules.notice.dao.MessageNotificationDao;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.entity.MessageNotificationEntity;
import io.renren.modules.notice.entity.NoticeStatus;
import io.renren.modules.notice.service.MessageNotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("messageNotificationService")
public class MessageNotificationServiceImpl extends ServiceImpl<MessageNotificationDao, MessageNotificationEntity> implements MessageNotificationService {

    /**
     * @description: 创建一条消息，发送给不同的人
     * @author: hong
     * @date: 2024/10/25 14:40
     * @version: 1.0
     */
    @Override
    public Boolean sendMessages(CreateNoticeParams params) {
        MessageNotificationEntity notification = new MessageNotificationEntity();
        notification.setSenderId(params.getSenderId());
        notification.setCreatedAt(new java.util.Date());
        notification.setStatus(String.valueOf(NoticeStatus.UNREAD));
        notification.setContent(params.getContent());
        notification.setType(params.getType());
        for (Long recordId : params.getReceiverId()) {
            notification.setReceiverId(recordId);
            this.save(notification);
        }
        return true;
    }

    @Override
    public int getUnreadNotices(Long userId) {
        QueryWrapper<MessageNotificationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId)
                .eq("status", String.valueOf(NoticeStatus.UNREAD));
        return this.count(queryWrapper); // 查询未读消息数量
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MessageNotificationEntity> page = this.page(
                new Query<MessageNotificationEntity>().getPage(params),
                new QueryWrapper<MessageNotificationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageMyNoticeList(Map<String, Object> params, Long userId) {
        IPage<MessageNotificationEntity> page = this.page(
                new Query<MessageNotificationEntity>().getPage(params),
                new QueryWrapper<MessageNotificationEntity>().eq("receiver_id", userId)
        );

        return new PageUtils(page);
    }



}