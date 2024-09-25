package io.renren.modules.qcManagement.dao;

import io.renren.modules.qcManagement.entity.QcStepEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 * @author hong
 * @email ${email}
 * @date 2024-08-25 14:45:45
 */
@Mapper
public interface QcStepDao extends BaseMapper<QcStepEntity> {

    @Select("select * from qc_step where step_subject_id = #{stepSubjectId}")
    List<QcStepEntity> getStepListById(Integer stepSubjectId);
}
