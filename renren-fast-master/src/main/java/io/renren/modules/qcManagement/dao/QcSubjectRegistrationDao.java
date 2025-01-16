package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcknowledgebaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
@Mapper
public interface QcSubjectRegistrationDao extends BaseMapper<QcSubjectRegistrationEntity> {

    List<QcknowledgebaseEntity> selectFinishedSubjectList(@Param("topicName") String topicName,
                                                          @Param("keywords") String keywords,
                                                          @Param("activityPlan") String activityPlan,
                                                          @Param("activityPlanEnd") String activityPlanEnd);

    //根据小组名称查询小组成员
    @Select({
            "SELECT * ",
            "FROM qc_group_member",
            "WHERE group_name = #{groupName}",
            "AND role_in_topic = '成员'"
    })
    List<QcGroupMemberEntity> getMembersOfGroup(@Param("groupName") String groupName);

    //模糊查询
    List<QcSubjectRegistrationEntity> fuzzyQueryList(@Param("topicName") String topicName, @Param("keywords") String keywords);

    //统计已注册课题数
    @Select({
            "SELECT COUNT(*)",
            "FROM qc_subject_registration",
    })
    Integer countRegistration();

    //统计已提交课题数
    @Select({
            "SELECT COUNT(*)",
            "FROM qc_subject_registration",
            "WHERE result_type  != null|| result_type != ''",
    })
    Integer countSubmitted();

    //查询编号最大值

 @Select("SELECT MAX(CAST(SUBSTRING_INDEX(topic_number, '-', -1) AS UNSIGNED)) AS max_id " +
            "FROM qc_subject_registration " +
            "WHERE topic_number LIKE CONCAT('PJHLQCKT-', #{currentYear}, '-%')")
    Integer maxOfId(@Param("currentYear") String currentYear);


    //查询课题名称是否重复
    boolean ifExistSubjectName(String name);

    //查询当前用户是否是组长
    boolean ifGroupLead(String userName);


    //修改入库标识
    @Update({
            "<script>",
            "UPDATE qc_subject_registration s",
            "LEFT JOIN qc_examine_status e ON s.qcsr_id = e.qc_examine_subject",
            "SET e.qc_storage_flag = 0",
            "WHERE s.qcsr_id IN",
            "<foreach item='id' index='index' collection='list' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void updateStorageFlagToZero(List<Long> qcsrIds);
}
//计算课题活动状态
//    @Select({
//            "SELECT *",
//            "FROM qc_subject_registration",
//            "WHERE topic_review_status = 3",
//
//    })
//    List<QcSubjectRegistrationEntity>  getActivity(String charId,String type);


