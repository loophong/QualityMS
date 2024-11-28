package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.generator.entity.IssueTableEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 问题表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
@Mapper
public interface IssueTableDao extends BaseMapper<IssueTableEntity> {

    Page<IssueTableEntity> selectFinishedSubjectList(Page<IssueTableEntity> page);
}
