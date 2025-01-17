package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IndemnificationTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2025-01-10 15:53:47
 */
public interface IndemnificationTableService extends IService<IndemnificationTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<IndemnificationTableEntity> getAllIndemnifications();

    IndemnificationTableEntity getIndemnificationById(int id);
}

