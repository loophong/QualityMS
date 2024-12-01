package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueTableEntity;
import org.apache.poi.ss.usermodel.Workbook;
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
    PageUtils queryPageByDescription(Map<String, Object> params, String description);//问题描述 模糊查询 修改
    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPagecreator(Map<String, Object> params);
    PageUtils QueryPage(Map<String, Object> params);

    PageUtils QueryFuPage(Map<String, Object> params);
    List<IssueTableEntity> listAll();
    String saveUploadedFile(MultipartFile file) throws IOException;

//    void updateIssuePhoto(String filePath);

    String getuserinfo();

    R closeRelatedTasks(Long issueId);

    R uploadExcelFile(MultipartFile file) throws IOException;

    /**
     * 统计当月各类问题数量
     */
    Map<String, Integer> getCurrentMonthVerificationConclusionStatistics();

    IssueTableEntity getByissueNumber(String issueNumber);

    Map<String, Integer> getTaskDetails(String issueNumber);

    void connectionIssue(Long issueId);

    Workbook generateTemplate(); // 声明生成模板的方法

    void removeAll(Integer[] issueIds);

    Map<String, Integer> getCurrentMonthCompletionRate();

    boolean checkDuplicateIssue(List<String> vehicleNumbers, String issueCategoryIds);

    IssueTableEntity getByassociate(String associatedRectificationRecords);

    boolean checkReplicateIssue(Integer issueId, String issueCategoryIds,String systematicClassification,String firstFaultyParts,String secondFaultyParts,String faultType,String faultModel);

    PageUtils queryPageFinishedList(Map<String, Object> params);
}

