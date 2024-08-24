package io.renren.modules.taskmanagement.dao;

import io.renren.modules.taskmanagement.entity.ApprovalEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务审批表
 * 
 * @author hong
 * @email ${email}
 * @date 2024-08-23 09:58:46
 */
@Mapper
public interface ApprovalDao extends BaseMapper<ApprovalEntity> {
	
}
