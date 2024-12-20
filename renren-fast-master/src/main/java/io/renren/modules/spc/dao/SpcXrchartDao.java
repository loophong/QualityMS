package io.renren.modules.spc.dao;

import io.renren.modules.spc.entity.SpcXrchartEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
@Mapper
public interface SpcXrchartDao extends BaseMapper<SpcXrchartEntity> {

    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);

    // 删除指定表名的数据
    void deleteDataByTableName(@Param("tableName") String tableName);

    // 批量插入数据
    void batchInsertSpcXrcharts(@Param("list") List<SpcXrchartEntity> list);

    List<SpcXrchartEntity> getSpcXrchartEntityByMonth(@Param("datatime") Date datatime);

    List<SpcXrchartEntity> getSpcXrchartEntityByTableName(@Param("tableName") String tableName);

    Double getConstantForXbarR_A2(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarR_D3(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarR_D4(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarS_A3(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarS_B3(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarS_B4(@Param("subarray") int subarray_capacity);

    Double getConstantForXbarS_A4(@Param("subarray") int subarray_capacity);

    Double getConstantForMeR_D3(@Param("subarray") int subarray_capacity);

    Double getConstantForMeR_D4(@Param("subarray") int subarray_capacity);

    Double getConstantForIMR_E2(@Param("subarray") int subarray_capacity);

    Double getConstantForIMR_D3(@Param("subarray") int subarray_capacity);

    Double getConstantForIMR_D4(@Param("subarray") int subarray_capacity);

    List<String> getTableName();
}
