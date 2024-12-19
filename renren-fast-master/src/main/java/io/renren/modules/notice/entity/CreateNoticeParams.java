package io.renren.modules.notice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateNoticeParams {
    /**
     * 发送者ID
     */
    private Long senderId;
    /**
     * 接收者ID
     */
    private Long[] receiverId;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 跳转位
     */
    private String jumpdepart;
}
