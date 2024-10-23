package io.renren.modules.qcManagement.dao;

import io.renren.modules.qcManagement.entity.QcToolsTemplateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface QcToolsTemplateDao extends BaseMapper<QcToolsTemplateEntity> {
    @Select("select * from qc_tools_template where template_type = #{templateType}")
    List<QcToolsTemplateEntity> getTemplateByName(String templateType);
}
