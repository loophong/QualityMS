package io.renren.modules.spc.dao;

import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;
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
 * @date 2024-12-14 20:55:08
 */
@Mapper
public interface SpcXrchartStandardsDao extends BaseMapper<SpcXrchartStandardsEntity> {
    // 删除指定月份的数据
    void deleteDataByMonth(@Param("datatime") Date datatime);
    // 批量插入数据
    void batchInsertSpcXrchartStandards(@Param("list") List<SpcXrchartStandardsEntity> list);

    List<SpcXrchartStandardsEntity> getSpcXrchartStandardsEntityByMonth(@Param("datatime") Date datatime);
}
