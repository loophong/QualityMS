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

    // 根据TSP查询
    @Select("select * from qc_tools_conplan where conplan_type = #{conplanType} and conplan_subject = #{conplanSubject} and conplan_process = #{conplanProcess}")
    List<QcToolsConplanEntity> getDataByTSP(String conplanType, Integer conplanSubject, Integer conplanProcess);

    // 根据T查询
    @Select("select * from qc_tools_conplan where conplan_type = #{conplanType} ")
    List<QcToolsConplanEntity> getDataByT(String conplanType);

    // 根据SP查询
    @Select("select * from qc_tools_conplan where conplan_subject = #{conplanSubject} and conplan_process = #{conplanProcess}")
    List<QcToolsConplanEntity> getDataBySP(Integer conplanSubject, Integer conplanProcess);

    // 根据S查询
    @Select("select * from qc_tools_conplan where conplan_subject = #{conplanSubject} ")
    List<QcToolsConplanEntity> getDataByS(Integer conplanSubject);

    @Select("select * from qc_tools_conplan where conplan_issue = #{conplanIssue} ")
    List<QcToolsConplanEntity> getDataByI(Integer conplanIssue);

    //根据Id查询
    @Select("select * from qc_tools_conplan where conplan_id = #{conplanId}")
    List<QcToolsConplanEntity> getDataById(Integer conplanId);
}
