package io.renren.modules.spc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.spc.entity.SpcXrchartEntity;
import io.renren.modules.spc.entity.SpcXrchartStandardsEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-14 20:55:08
 */
public interface SpcXrchartStandardsService extends IService<SpcXrchartStandardsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void importData(List<SpcXrchartStandardsEntity> datalist);

}

