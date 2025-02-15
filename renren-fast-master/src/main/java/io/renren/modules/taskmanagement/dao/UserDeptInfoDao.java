package io.renren.modules.taskmanagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.app.entity.UserEntity;
import io.renren.modules.taskmanagement.entity.TaskDataTaskEntity;
import io.renren.modules.taskmanagement.entity.UserDeptInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDeptInfoDao extends BaseMapper<UserEntity> {

    @Select("SELECT dept, user_id, username, nickname FROM sys_user ORDER BY dept")
    List<UserDeptInfoDTO> getEmployeesGroupedByDepartment();

}
