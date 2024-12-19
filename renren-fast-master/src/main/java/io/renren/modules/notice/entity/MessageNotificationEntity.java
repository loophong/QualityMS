package io.renren.modules.notice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-10-25 10:34:11
 */
@Data
@TableName("message_notification")
public class MessageNotificationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 接收者ID
	 */
	private Long receiverId;
	/**
	 * 发送者ID
	 */
	private Long senderId;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息类型
	 */
	private String type;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 消息状态
	 */
	private String status;
	/**
	 * 跳转位
	 */
	private String jumpdepart;

}
