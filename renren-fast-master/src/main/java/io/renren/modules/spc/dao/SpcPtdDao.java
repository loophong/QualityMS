package io.renren.modules.spc.dao;

import io.renren.modules.spc.entity.SpcPtdEntity;
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
 * @date 2024-12-03 15:22:22
 */
@Mapper
public interface SpcPtdDao extends BaseMapper<SpcPtdEntity> {
    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);

    // 批量插入数据
    void batchInsertSpcPtdcharts(@Param("list") List<SpcPtdEntity> list);

    //按月份获取数据
    List<SpcPtdEntity> getSpcPtdEntityByMonth(@Param("datatime") Date datatime);
}
