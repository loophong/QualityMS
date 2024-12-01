package io.renren.modules.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.notice.entity.CreateNoticeParams;
import io.renren.modules.notice.entity.MessageNotificationEntity;


import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-25 10:34:11
 */
public interface MessageNotificationService extends IService<MessageNotificationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageMyNoticeList(Map<String, Object> params, Long userId);

    Boolean sendMessages(CreateNoticeParams params);

    int getUnreadNotices(Long userId);

}

