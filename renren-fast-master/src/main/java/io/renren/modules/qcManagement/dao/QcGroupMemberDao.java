package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import io.renren.modules.qcManagement.entity.QcStepEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
@Mapper
public interface QcGroupMemberDao extends BaseMapper<QcGroupMemberEntity> {


    //QC小组注册数
    @Select({
            "SELECT COUNT(*)  ",
            "FROM qc_group_members ",
            "WHERE role_in_topic = '组长' ",
            "AND delete_flag = 0"
    })
    Integer countRegistrationNumbers();

    //有课题的QC小组数
    @Select({
            "SELECT COUNT(DISTINCT group_name)   ",
            "FROM qc_subject_registration",
            "WHERE topic_review_status != '完成' "
    })
    Integer countHaveRegistrationNumbers();

    //QC小组注册人数
//    @Select({
//            "SELECT COUNT(*) AS uniqueRecordCount ",
//            "FROM (",
//            "    SELECT name, number ",
//            "    FROM qc_group_members ",
//            "    GROUP BY name, number",
//            ") AS unique_records"
//    })
//    Integer countMembersUnique();
        @Select({
            "SELECT COUNT(DISTINCT name) ",
            "FROM qc_group_members ",
            "WHERE delete_flag = 0"
         })
        Integer countMembersUnique();

    @Select("select * from qc_group_members where parent_id = #{parentId}")
    List<QcGroupMemberEntity> getChildrenListById(Integer parentId);

    //查询已通过审核的小组
    @Select("SELECT * FROM qc_group_members WHERE delete_flag != 1 AND examine_status = '通过'")
    List<QcGroupMemberEntity> myGetGroupMemberList();

    //查询所有未删除的小组
    @Select("SELECT * FROM qc_group_members WHERE delete_flag != 1")
    List<QcGroupMemberEntity> myAllGroupMemberList();

//    @Select("SELECT * FROM qc_group_members WHERE delete_flag != 1 AND examine_status = '未通过'")
//    List<QcGroupMemberEntity> examineGroupMemberList();

//    @Select("SELECT * FROM qc_group_members WHERE parent_id = #{id} AND delete_flag != 1")
//    List<QcGroupMemberEntity> aboutMeList( int id);
    //注册号
    @Select({
            "SELECT COUNT(*) ",
            "FROM qc_group_members ",
            "WHERE YEAR(participation_date) = YEAR(CURRENT_DATE) AND role_in_topic = '组长' AND registration_num IS NOT NULL"
    })
    Integer registrationCount();
}
