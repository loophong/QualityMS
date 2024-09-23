package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueTableEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 问题表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
public interface IssueTableService extends IService<IssueTableEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<IssueTableEntity> listAll();
    String saveUploadedFile(MultipartFile file) throws IOException;

//    void updateIssuePhoto(String filePath);

    String getuserinfo();

    R closeRelatedTasks(Long issueId);

    R uploadExcelFile(MultipartFile file) throws IOException;

}

