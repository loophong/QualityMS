package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.DepartmentTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-11 15:25:55
 */
public interface DepartmentTableService extends IService<DepartmentTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DepartmentTableEntity> listAll();
}

