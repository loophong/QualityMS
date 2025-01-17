package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.VendorTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-10 15:54:12
 */
public interface VendorTableService extends IService<VendorTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<VendorTableEntity> listAll();
}

