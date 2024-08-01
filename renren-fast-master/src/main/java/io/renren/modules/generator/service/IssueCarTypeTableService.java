package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IssueCarTypeTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 车型字典
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 18:55:06
 */
public interface IssueCarTypeTableService extends IService<IssueCarTypeTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<IssueCarTypeTableEntity> listAll();
}

