package io.renren.modules.qcManagement.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.qcManagement.entity.QcToolsConplanEntity;
import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-20 17:53:45
 */
@Mapper
public interface QcToolsConplanDao extends BaseMapper<QcToolsConplanEntity> {

    @Select("select * from qc_tools_conplan where conplan_type = #{conplanType} and conplan_subject = #{conplanSubject} and conplan_process = #{conplanProcess}")
    List<QcToolsConplanEntity> getDataByTSP(String conplanType, Integer conplanSubject, Integer conplanProcess);

}
