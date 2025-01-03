package io.renren.modules.spc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.spc.entity.SpcPchartEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-27 10:49:53
 */
public interface SpcPchartService extends IService<SpcPchartEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void importData(List<SpcPchartEntity> datalist);

    List<List<Double>> getPChart(String tableName);

    List<List<Double>> getNPChart(String tableName);

    List<List<Double>> getUChart(String tableName);

    List<List<Double>> getCChart(String tableName);

    List<String> getTableName();
}

