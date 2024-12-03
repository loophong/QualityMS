package io.renren.modules.spc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.spc.entity.SpcPtdEntity;
import io.renren.modules.spc.entity.SpcXrchartEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-03 15:22:22
 */
public interface SpcPtdService extends IService<SpcPtdEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void importData(List<SpcPtdEntity> datalist);

    List<List<Double>> getPTDChart();
}

