package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.IssueTypeTableEntity;
import io.renren.modules.generator.entity.IssuetypeGradeTableEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-25 14:50:00
 */
public interface IssuetypeGradeTableService extends IService<IssuetypeGradeTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<IssuetypeGradeTableEntity> listAll();
}

