package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * QC小组成员信息表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-19 10:16:22
 */
@Mapper
public interface QcGroupMemberDao extends BaseMapper<QcGroupMemberEntity> {

}
