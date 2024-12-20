package io.renren.modules.spc.dao;
import io.renren.modules.spc.entity.SpcPchartEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
@Mapper
public interface SpcPchartDao extends BaseMapper<SpcPchartEntity> {

    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);
    // 删除指定表名的数据
    void deleteDataByTableName(@Param("tableName") String tableName);

    // 批量插入数据
    void batchInsertSpcPcharts(@Param("list") List<SpcPchartEntity> list);

    //根据月份获取数据
    List<SpcPchartEntity> getSpcPchartEntityByMonth(@Param("datatime") Date datatime);

    //根据表名获取数据
    List<SpcPchartEntity> getSpcPchartEntityByTableName(@Param("tableName") String tableName);

    //获取表名
    List<String> getTableName();
}
