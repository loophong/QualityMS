package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.TestTableEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-18 08:58:50
 */
public interface TestTableService extends IService<TestTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

