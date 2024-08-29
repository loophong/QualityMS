package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    @Select({
            "SELECT *",
            "FROM qc_group_member",
            "WHERE group_name = #{groupName}",
            "AND role_in_topic = '成员'"
    })
    List<QcGroupMemberEntity> getMembersOfGroup(@Param("groupName") String groupName);
}
