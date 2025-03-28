package io.renren.modules.qcManagement.dao;

import io.renren.modules.qcManagement.entity.QcExamineStatusEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    //统计未审核完成课题数
    @Select({
            "SELECT COUNT(*)",
            "FROM qc_examine_status",
            "WHERE qc_examine_current  != '完成'",
    })
    Integer countWithoutExamined();

    //统计已获奖课题数
    @Select({
            "SELECT COUNT(*)",
            "FROM qc_subject_registration",
            "WHERE topic_activity_result IN ('一等奖', '二等奖', '三等奖', '四等奖', '鼓励奖')",
    })
    Integer countExamined();
}
