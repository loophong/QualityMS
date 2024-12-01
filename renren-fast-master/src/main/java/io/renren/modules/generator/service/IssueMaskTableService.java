package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IssueMaskTableEntity;

import java.util.List;
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

    PageUtils managerqueryPage(Map<String, Object> params);

    PageUtils AuditqueryPage(Map<String, Object> params);

    List<IssueMaskTableEntity> listAll(String issueNumber);

    String listRecords(String issueNumber);

    String dispatch(String issueNumber ,String serialNumber);

    PageUtils acceptqueryPage(Map<String, Object> params);

    PageUtils distributequeryPage(Map<String, Object> params);
}

