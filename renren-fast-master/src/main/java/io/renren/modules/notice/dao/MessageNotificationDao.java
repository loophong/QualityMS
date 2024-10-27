package io.renren.modules.notice.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.notice.entity.MessageNotificationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-10-25 10:34:11
 */
@Mapper
public interface MessageNotificationDao extends BaseMapper<MessageNotificationEntity> {
	
}
