package io.renren.modules.taskmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.taskmanagement.entity.PlanApprovalTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务审批表
 * 
 * @author sun
 * @email ${email}
 * @date 2024-12-15 14:35:09
 */
@Mapper
public interface PlanApprovalTableDao extends BaseMapper<PlanApprovalTableEntity> {
	
}
