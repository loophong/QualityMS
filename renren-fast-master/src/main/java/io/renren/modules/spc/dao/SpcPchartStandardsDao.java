package io.renren.modules.spc.dao;

import io.renren.modules.spc.entity.SpcPchartStandardsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-12-14 20:55:08
 */
@Mapper
public interface SpcPchartStandardsDao extends BaseMapper<SpcPchartStandardsEntity> {
    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);
    // 删除指定表名的数据
    void deleteDataByTableName(@Param("tableName") String tableName);
    // 批量插入数据
    void batchInsertSpcPchartStandards(@Param("list") List<SpcPchartStandardsEntity> list);

    List<SpcPchartStandardsEntity> getSpcPchartStandardsEntityByMonth(@Param("datatime") Date datatime);

    List<SpcPchartStandardsEntity> getSpcPchartStandardsEntityByTableName(@Param("tableName") String tableName);
}
