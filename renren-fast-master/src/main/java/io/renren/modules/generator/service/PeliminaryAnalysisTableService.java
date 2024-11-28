package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.PeliminaryAnalysisTableEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-20 15:20:17
 */
public interface PeliminaryAnalysisTableService extends IService<PeliminaryAnalysisTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PeliminaryAnalysisTableEntity> listAll();

    R uploadExcelFile(MultipartFile file);
}

