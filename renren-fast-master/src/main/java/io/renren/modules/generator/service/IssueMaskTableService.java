package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IssueMaskTableEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 16:54:01
 */
public interface IssueMaskTableService extends IService<IssueMaskTableEntity> {

    PageUtils recipientsqueryPage(Map<String, Object> params);

    PageUtils creatorqueryPage(Map<String, Object> params);

    PageUtils AuditqueryPage(Map<String, Object> params);
}
