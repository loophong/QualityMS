package io.renren.modules.spc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.spc.entity.SpcXrchartEntity;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
public interface SpcXrchartService extends IService<SpcXrchartEntity> {



    PageUtils queryPage(Map<String, Object> params);


    void importData(List<SpcXrchartEntity> datalist);

    //12/18 修改1&2&3&4 原：通过时间获取数据。现：通过name获取数据
    //修改1
    List<List<Double>> getXbar_RChart(String tableName);

    //修改2
    List<List<Double>> getXbar_SChart(String tableName);

    //修改3
    List<List<Double>> getMe_RChart(String tableName);

    //修改4
    List<List<Double>> getI_MRChart(String tableName);

    List<String> getTableName();
}

