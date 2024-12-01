package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.qcManagement.entity.QcknowledgebaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
@Mapper
public interface IssueTableDao extends BaseMapper<IssueTableEntity> {


    List<IssueTableEntity> selectFinishedSubjectList(@Param("creationTime") String creationTime, @Param("issueDescription") String issueDescription);

    Page<IssueTableEntity> selectFinishedSubjectListByDescription(Page<IssueTableEntity> page, String description);
}
