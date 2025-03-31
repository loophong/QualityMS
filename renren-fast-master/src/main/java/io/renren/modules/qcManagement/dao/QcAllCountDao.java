package io.renren.modules.qcManagement.dao;

import io.lettuce.core.dynamic.annotation.Param;
import io.renren.modules.qcManagement.entity.QcAllCountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.qcManagement.entity.QcGroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlProvider;

import java.util.List;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-13 20:23:51
 */
@Mapper
public interface QcAllCountDao extends BaseMapper<QcAllCountEntity> {

}
