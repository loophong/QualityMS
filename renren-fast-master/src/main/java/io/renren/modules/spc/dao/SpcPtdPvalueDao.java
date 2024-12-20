package io.renren.modules.spc.dao;

import io.renren.modules.spc.entity.SpcPtdEntity;
import io.renren.modules.spc.entity.SpcPtdPvalueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-12-19 16:13:33
 */
@Mapper
public interface SpcPtdPvalueDao extends BaseMapper<SpcPtdPvalueEntity> {

    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);
    // 删除指定表名的数据
    void deleteDataByTableName(@Param("tableName") String tableName);

    // 批量插入数据
    void batchInsertSpcPtdPValueCharts(@Param("list") List<SpcPtdPvalueEntity> list);

    //按照月份获取数据
    List<SpcPtdPvalueEntity> getSpcPtdPValueEntityByMonth(@Param("datatime") Date datatime);

    //按照表名获取数据
    List<SpcPtdPvalueEntity> getSpcPtdPValueEntityByTableName(@Param("tableName") String tableName);
}
