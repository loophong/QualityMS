package io.renren.modules.notice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.ShiroUtils;
import io.renren.modules.notice.entity.MessageNotificationEntity;
import io.renren.modules.notice.entity.NoticeStatus;
import io.renren.modules.notice.service.MessageNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-25 10:34:11
 */
@Slf4j
@RestController
@RequestMapping("notice")
public class MessageNotificationController {
    @Autowired
    private MessageNotificationService messageNotificationService;

    /**
     * @description: 查询是否有未读消息
     */
    @RequestMapping("/Unread")
    @RequiresPermissions("notice:messagenotification:list")
    public R getUnreadNotice(@RequestParam Map<String, Object> params){
        Long userId = ShiroUtils.getUserId(); // 获取当前登录用户的ID
        System.out.println("f："+userId+"-------------");
        int unreadNotices = messageNotificationService.getUnreadNotices(userId);
        System.out.println("未读信息为："+unreadNotices+"-------------");
        return R.ok().put("hasUnread", unreadNotices > 0 ? 1 : 0);
    }

    /**
     * @description: 查询我的消息列表
     * @author: hong
     * @date: 2024/10/25 10:55
     * @version: 1.0
     */
    @RequestMapping("/mine")
    @RequiresPermissions("notice:messagenotification:list")
    public R getMyNoticeList(@RequestParam Map<String, Object> params){
        PageUtils page = messageNotificationService.queryPageMyNoticeList(params, ShiroUtils.getUserId());

        return R.ok().put("page", page);
    }

    /**
     * @description: 新增消息
     * @param: 发送者id，接受者id，消息内容，消息类型[模块]，消息跳转位[1.问题详情页，2.问题下任务审核页，3.问题下任务执行页，4.任务，5.其他]
     * @author: hong
     * @date: 2024/10/25 11:10
     * @version: 1.0
     */
    @RequestMapping("/save")
    @RequiresPermissions("notice:messagenotification:save")
    public R save(@RequestBody MessageNotificationEntity messageNotification){
        messageNotification.setCreatedAt(new java.util.Date());
        messageNotification.setStatus(String.valueOf(NoticeStatus.UNREAD));
        System.out.println("messageNotification："+messageNotification);
        messageNotificationService.save(messageNotification);
        return R.ok();
    }

    /**
     * @description: 阅读消息
     * @author: hong
     * @date: 2024/10/25 14:28
     * @version: 1.0
     */
    @GetMapping("/readNotice")
//    @RequiresPermissions("notice:messagenotification:readNotice")
    public R readNotice(@RequestParam Long id){
        log.info("noticeId"+id);
        MessageNotificationEntity notification = messageNotificationService.getById(id);
        notification.setStatus(String.valueOf(NoticeStatus.READ));
        messageNotificationService.updateById(notification);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("notice:messagenotification:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageNotificationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("notice:messagenotification:info")
    public R info(@PathVariable("id") Long id){
        MessageNotificationEntity messageNotification = messageNotificationService.getById(id);

        return R.ok().put("messageNotification", messageNotification);
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("notice:messagenotification:update")
    public R update(@RequestBody MessageNotificationEntity messageNotification){
        messageNotificationService.updateById(messageNotification);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("notice:messagenotification:delete")
    public R delete(@RequestBody Long[] ids){
        messageNotificationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
