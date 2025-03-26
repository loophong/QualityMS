package io.renren.modules.qcManagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.qcManagement.entity.QcAllCountEntity;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author sun
 * @email ${email}
 * @date 2024-10-13 20:23:51
 */
public interface QcAllCountService extends IService<QcAllCountEntity> {

    PageUtils queryPage(Map<String, Object> params);


}

