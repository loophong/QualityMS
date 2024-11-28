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

    // 批量插入数据
    void batchInsertSpcXrcharts(@Param("list") List<SpcXrchartEntity> list);

}
