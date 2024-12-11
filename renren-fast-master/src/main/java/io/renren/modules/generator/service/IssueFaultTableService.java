package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueFaultTableEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 
 *
 * @author sun
 * @email ${email}
 * @date 2024-11-20 15:20:56
 */
public interface IssueFaultTableService extends IService<IssueFaultTableEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R uploadExcelFile(MultipartFile file) throws IOException;

    PageUtils QueryPage(Map<String, Object> params);
}

