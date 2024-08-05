package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IssueTypeTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 问题类别字典
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-23 10:59:16
 */
public interface IssueTypeTableService extends IService<IssueTypeTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<IssueTypeTableEntity> listAll();
}

