package io.renren.modules.taskmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.taskmanagement.entity.FileEntity;
import io.renren.modules.taskmanagement.entity.PlanEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao extends BaseMapper<FileEntity> {
}
