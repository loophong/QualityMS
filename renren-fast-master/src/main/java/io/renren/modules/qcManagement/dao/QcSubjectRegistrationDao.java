package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.qcManagement.entity.QcSubjectRegistrationEntity;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 12:37:20
 */
@Mapper
public interface QcSubjectRegistrationDao extends BaseMapper<QcSubjectRegistrationEntity> {

    Page<QcSubjectRegistrationEntity> selectFinishedSubjectList(Page<QcSubjectRegistrationEntity> page);

    //根据小组名称查询小组成员
    @Select({
            "SELECT *",
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
    //计算课题活动状态
//    @Select({
//            "SELECT *",
//            "FROM qc_subject_registration",
//            "WHERE topic_review_status = 3",
//
//    })
//    List<QcSubjectRegistrationEntity>  getActivity(String charId,String type);

}
