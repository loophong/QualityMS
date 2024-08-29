package io.renren.modules.qcManagement.dao;

import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 *
 * @author lin
 * @email ${email}
 * @date 2024-08-27 14:22:12
 */
@Mapper
public interface QcExamineStatusDao extends BaseMapper<QcExamineStatusEntity> {
    boolean existsBySubject(Integer qcExamineSubject);

    void deleteBySubject(Integer qcExamineSubject);

}
