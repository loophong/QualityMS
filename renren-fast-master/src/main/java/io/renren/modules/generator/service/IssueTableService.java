package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    R closeRelatedTasks(Long issueId, Integer closeRelated);

    R uploadExcelFile(MultipartFile file) throws IOException;

    /**
     * 统计当月各类问题数量
     */
    Map<String, Integer> getCurrentMonthVerificationConclusionStatistics();

    Map<String, Integer> getCurrentMonthStatistics();

    IssueTableEntity getByissueNumber(String issueNumber);

    Map<String, Integer> getTaskDetails(String issueNumber);

    void connectionIssue(Long issueId);

    Workbook generateTemplate(); // 声明生成模板的方法

    void removeAll(Integer[] issueIds);

    Map<String, Integer> getCurrentMonthCompletionRate();

    boolean checkDuplicateIssue(List<String> vehicleNumbers, String issueCategoryIds);

    IssueTableEntity getByassociate(String associatedRectificationRecords);

    boolean checkReplicateIssue(Integer issueId, String systematicClassification,String firstFaultyParts,String secondFaultyParts,String faultType,String faultModel);

    PageUtils queryPageFinishedList(Map<String, Object> params);


    String newIssueNumber();

    String connectNumber(Integer id);





    Map<String, Integer> getMonthlyCountByYear(int year); // 月度统计

    Map<String, Integer> getmonthlyDuplicateStats(int year);

    Map<String, Integer> getcurrentMonthInProgressCategoryStats(String startDate, String endDate);  // 当前月进行中类别统计


    List<IssueTableEntity> listAll01(Map<String, Object> params);

    Map<String, Integer> gettruecurrentall();

    boolean updateParentQuestion(String issueNumber);

    boolean clearStorageFlag(List<Integer> ids);//删除功能

    int getallissue();

    Map<String, Object> getThirdIssue();

    Map<String, Map<String, Integer>> getIssuebyDepartment(String startDate, String endDate);

    Map<String, Map<String, Object>> getbyindemnification(String startDate, String endDate);

    Map<String, Map<String, Object>> getbyvendor(String startDate, String endDate);

    Map<String, Map<String, Object>> getbyvehicletype(String startDate, String endDate);

    Map<String, Map<String, Object>> getbyregiontype(String startDate, String endDate);
}

