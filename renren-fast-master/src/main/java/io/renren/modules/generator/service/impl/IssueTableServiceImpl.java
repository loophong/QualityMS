package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.config.FileUploadProperties;
import io.renren.modules.generator.dao.IssueTableDao;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service("issueTableService")
public class IssueTableServiceImpl extends ServiceImpl<IssueTableDao, IssueTableEntity> implements IssueTableService {
    private final String uploadDir;

//    @Value("${file.upload.dir}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

//    @Value("${server.address}")
    private String serverAddress;

    @Autowired
    public IssueTableServiceImpl(FileUploadProperties fileUploadProperties) {
        this.uploadDir = fileUploadProperties.getUploadDir();
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                new QueryWrapper<IssueTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<IssueTableEntity> listAll() {
        System.out.println("///" + list());
        return this.list();
    }

    @Override
    public String saveUploadedFile(MultipartFile file) throws IOException {
//        if (uploadPath == null) {
//            throw new IllegalStateException("Upload path is not configured.");
//        }

        // 创建目标文件路径
        String originalFilename = file.getOriginalFilename();
        String filePath = uploadPath + File.separator + originalFilename;
        File dest = new File(filePath);

        // 检查目录是否存在，不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        // 直接保存文件到目标路径
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // 生成文件URL
        String fileUrl = generateFileUrl(originalFilename);
        return fileUrl;
    }

    private String generateFileUrl(String filename) {
        return "http://" + serverAddress + ":" + serverPort + contextPath + "/upload/" + filename;
    }
//    }
//        // 创建目标文件路径
//        File dest = new File(uploadPath + file.getOriginalFilename());
//
//        // 检查目录是否存在，不存在则创建
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//
//        // 保存文件到目标路径
//        file.transferTo(dest);
//
//        return dest.getAbsolutePath();


//    @Override
//    public void updateIssuePhoto(Integer issueId, String imagePath) {
//        IssueTableEntity issueTable = this.getById(issueId);
//        if (issueTable != null) {
//            issueTable.setIssuePhoto(imagePath);
//            this.updateById(issueTable);
//        }
//    }

    @Override
    public String getuserinfo() {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        return rolename;
    }

    @Override
    public R closeRelatedTasks(Long issueId) {
        // 通过 ID 获取对应的实体类
        IssueTableEntity issueTable = this.getById(issueId);
        if (issueTable == null) {
            return R.error("未找到该问题");
        }

        // 打印当前问题的基本信息
        System.out.println("当前问题主键ID: " + issueTable.getIssueId());
        System.out.println("当前问题编号: " + issueTable.getIssueNumber());

        // 获取与当前问题相关的所有问题编号
        String relatedIssueIdsString = issueTable.getAssociatedIssueAddition(); // 获取关联问题编号字符串
        List<String> relatedIssueNumbers = parseRelatedIssueIds(relatedIssueIdsString); // 使用解析方法

        // 打印拆分出的关联问题编号
        if (!relatedIssueNumbers.isEmpty()) {
            System.out.println("相关问题编号: " + String.join(", ", relatedIssueNumbers)); // 打印拆分后的关联问题编号
        } else {
            System.out.println("没有找到相关问题编号");
        }

        // 查找实际的ID
        List<Long> relatedIssueIds = new ArrayList<>(); // 存储数据库中的问题ID
        for (String number : relatedIssueNumbers) {
            // 根据编号查找对应的ID
            IssueTableEntity relatedIssue = this.list(Wrappers.<IssueTableEntity>lambdaQuery()
                    .eq(IssueTableEntity::getIssueNumber, number)).stream().findFirst().orElse(null);
            if (relatedIssue != null) {
                relatedIssueIds.add(Long.valueOf(relatedIssue.getIssueId())); // 假设数据表中有这个字段
                // 打印找到的关联问题的基本信息
                System.out.println("找到相关问题的主键ID: " + relatedIssue.getIssueId() + ", 问题编号: " + relatedIssue.getIssueNumber());
            } else {
                System.out.println("未找到编号为 " + number + " 的相关问题");
            }
        }

        // 打印将要删除的相关问题ID
        if (!relatedIssueIds.isEmpty()) {
            System.out.println("准备删除的关联问题ID: " + String.join(", ", relatedIssueIds.stream().map(String::valueOf).collect(Collectors.toList())));
        } else {
            System.out.println("没有要删除的关联问题ID");
        }

        try {
            // 删除所有关联的问题ID
            if (!relatedIssueIds.isEmpty()) {
                this.removeByIds(relatedIssueIds); // 执行批量删除
            }
            // 删除当前问题
//            this.removeById(issueId);
//            System.out.println("当前问题ID " + issueId + " 已成功删除");

            return R.ok().put("message", "相关任务和当前问题已成功关闭");
        } catch (Exception e) {
            e.printStackTrace(); // 输出异常信息
            return R.error("关闭任务失败: " + e.getMessage());
        }
    }

    @Override
    public R uploadExcelFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return R.error("请上传有效的Excel文件");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个sheet
            List<IssueTableEntity> issueList = new ArrayList<>();

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                // 跳过第一行（标题行）
                if (rowIndex == 0) {
                    continue; // 继续到下一循环，不处理这一行
                }

                IssueTableEntity issue = new IssueTableEntity();

                // 读取序列号
                issue.setSerialNumber(getCellValueAsString(row.getCell(0)));

                // 读取问题编号
                issue.setIssueNumber(getCellValueAsString(row.getCell(1)));

                // 读取检查部门
                issue.setInspectionDepartment(getCellValueAsString(row.getCell(2)));

                // 日期
                Cell dateCell = row.getCell(3);
                if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(dateCell)) {
                    Date inspectionDate = dateCell.getDateCellValue();
                    issue.setInspectionDate(inspectionDate);
                } else {
                    issue.setInspectionDate(null);
                }

                // 读取问题分类ID
                issue.setIssueCategoryId(getCellValueAsString(row.getCell(4)));

                // 读取车辆类型ID
                issue.setVehicleTypeId(getCellValueAsString(row.getCell(5)));

                // 读取车辆号码ID
                issue.setVehicleNumberId(getCellValueAsString(row.getCell(6)));

                // 读取问题描述
                issue.setIssueDescription(getCellValueAsString(row.getCell(7)));

                // 读取整改要求
                issue.setRectificationRequirement(getCellValueAsString(row.getCell(8)));

                // 读取要求完成时间
                Cell requiredCompletionTimeCell = row.getCell(9);
                if (requiredCompletionTimeCell != null && requiredCompletionTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(requiredCompletionTimeCell)) {
                    Date requiredCompletionTime = requiredCompletionTimeCell.getDateCellValue();
                    issue.setRequiredCompletionTime(requiredCompletionTime);
                } else {
                    issue.setRequiredCompletionTime(null);
                }

                // 读取责任部门
                issue.setResponsibleDepartment(getCellValueAsString(row.getCell(10)));

                // 读取整改状态
                issue.setRectificationStatus(getCellValueAsString(row.getCell(11)));

                // 读取实际完成时间
                Cell actualCompletionTimeCell = row.getCell(12);
                if (actualCompletionTimeCell != null && actualCompletionTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(actualCompletionTimeCell)) {
                    Date actualCompletionTime = actualCompletionTimeCell.getDateCellValue();
                    issue.setActualCompletionTime(actualCompletionTime);
                } else {
                    issue.setActualCompletionTime(null);
                }

                // 读取整改责任人
                issue.setRectificationResponsiblePerson(getCellValueAsString(row.getCell(13)));

                // 读取要求二次整改时间
                Cell requiredSecondRectificationTimeCell = row.getCell(14);
                if (requiredSecondRectificationTimeCell != null && requiredSecondRectificationTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(requiredSecondRectificationTimeCell)) {
                    Date requiredSecondRectificationTime = requiredSecondRectificationTimeCell.getDateCellValue();
                    issue.setRequiredSecondRectificationTime(requiredSecondRectificationTime);
                } else {
                    issue.setRequiredSecondRectificationTime(null);
                }

                // 读取备注
                issue.setRemark(getCellValueAsString(row.getCell(15)));

                // 读取创建人
                issue.setCreator(getCellValueAsString(row.getCell(16)));

                // 读取创建时间
                Cell creationTimeCell = row.getCell(17);
                if (creationTimeCell != null && creationTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(creationTimeCell)) {
                    Date creationTime = creationTimeCell.getDateCellValue();
                    issue.setCreationTime(creationTime);
                } else {
                    issue.setCreationTime(null);
                }

                // 读取最后修改人
                issue.setLastModifier(getCellValueAsString(row.getCell(18)));

                // 读取最后修改时间
                Cell lastModificationTimeCell = row.getCell(19);
                if (lastModificationTimeCell != null && lastModificationTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(lastModificationTimeCell)) {
                    Date lastModificationTime = lastModificationTimeCell.getDateCellValue();
                    issue.setLastModificationTime(lastModificationTime);
                } else {
                    issue.setLastModificationTime(null);
                }

                // 读取关联整改记录
                issue.setAssociatedRectificationRecords(getCellValueAsString(row.getCell(20)));

                // 读取关联问题编号
                issue.setAssociatedIssueAddition(getCellValueAsString(row.getCell(21)));

                // 读取创建时长
                issue.setCreationDuration(getCellValueAsString(row.getCell(22)));

                // 读取原因分析
                issue.setCauseAnalysis(getCellValueAsString(row.getCell(23)));

                // 读取整改验证状态
                issue.setRectificationVerificationStatus(getCellValueAsString(row.getCell(24)));

                // 读取验证结论
                issue.setVerificationConclusion(getCellValueAsString(row.getCell(25)));

                // 读取验证人
                issue.setVerifier(getCellValueAsString(row.getCell(26)));

                // 读取公式
                issue.setFormula(getCellValueAsString(row.getCell(27)));

                // 将 issue 对象添加到列表中
                issueList.add(issue);
            }


            // 保存到数据库
            this.saveBatch(issueList); // 批量保存
            System.out.println("上传--------------结束");
            return R.ok().put("message", "上传成功，已保存 " + issueList.size() + " 条数据");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    @Override
    public Map<String, Integer> getCurrentMonthVerificationConclusionStatistics() {
        Map<String, Integer> statistics = new HashMap<>();

        // 定义可能的验证结论状态
        List<String> verificationConclusions = Arrays.asList("持续", "未完成", "已完成", "结项");

        // 获取当前月份起止日期
        String currentMonthStart = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
        LocalDate nextMonthFirstDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        String nextMonthFirstDayString = nextMonthFirstDay.format(DateTimeFormatter.ISO_DATE);
//        String currentMonthEnd = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // 统计“创建”的数量（即 verification_conclusion 是 NULL 或空字符串的记录）
        statistics.put("未完成", countIssuesByCreationCondition(currentMonthStart, nextMonthFirstDayString));

        // 对其他验证结论进行统计
        for (String conclusion : verificationConclusions) {
            statistics.put(conclusion, countIssuesByVerificationConclusion(conclusion, currentMonthStart, nextMonthFirstDayString));
        }

        return statistics;
    }

    // 统计“创建”的条件
    private Integer countIssuesByCreationCondition(String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                        wrapper.isNull("verification_conclusion")
                                .or().eq("verification_conclusion", "")
                )
                .ge("creation_time", startDate)
                .le("creation_time", endDate);

        return this.count(queryWrapper);
    }

    // 根据 verification_conclusion 统计数量，处理包含多个状态的组合
    private Integer countIssuesByVerificationConclusion(String conclusion, String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        // 使用 FIND_IN_SET() 函数判断字段中是否包含该状态
        queryWrapper.ge("creation_time", startDate)
                .le("creation_time", endDate)
                .like("verification_conclusion", conclusion); // 使用 LIKE 查询

        return this.count(queryWrapper);
    }





    // 解析方法
    private List<String> parseRelatedIssueIds(String relatedIssueIds) {
        if (relatedIssueIds != null && !relatedIssueIds.trim().isEmpty()) {
            return Arrays.stream(relatedIssueIds.split(","))
                    .map(String::trim) // 去除每个编号的空格
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // 如果没有记录，则返回空列表
    }



    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }




}
