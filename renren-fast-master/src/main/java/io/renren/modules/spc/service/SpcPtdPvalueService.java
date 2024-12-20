package io.renren.modules.spc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.spc.entity.SpcPtdEntity;
import io.renren.modules.spc.entity.SpcPtdPvalueEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-12-19 16:13:33
 */
public interface SpcPtdPvalueService extends IService<SpcPtdPvalueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void importData(List<SpcPtdPvalueEntity> datalist);
}

