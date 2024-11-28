package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.utils.ShiroUtils;
import io.renren.config.FileUploadProperties;
import io.renren.modules.generator.dao.IssueMaskTableDao;
import io.renren.modules.generator.dao.IssueTableDao;
import io.renren.modules.generator.entity.IssueMaskTableEntity;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service("issueTableService")
public class IssueTableServiceImpl extends ServiceImpl<IssueTableDao, IssueTableEntity> implements IssueTableService {
    private final String uploadDir;
    @Autowired
    private IssueTableDao issueTableDao;
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
    public PageUtils queryPageFinishedList(Map<String, Object> params) {
        log.info("param"+params.get("page")+"------"+ params.get("limit"));

        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<IssueTableEntity> page = new Page<IssueTableEntity>(p,l);

        Page<IssueTableEntity> result = issueTableDao.selectFinishedSubjectList(page);
        log.info("result"+result);
        return new PageUtils(page);
    }

    @Override
    public String newIssueNumber() {
        // 查询所有的问题
        List<IssueTableEntity> issues = this.list();

        // 如果数据库为空，返回默认值 "0001"
        if (issues.isEmpty()) {
            return "0001";
        }

        // 找到 ID 最大的问题
        IssueTableEntity maxIssue = issues.stream()
                .max(Comparator.comparingLong(IssueTableEntity::getIssueId)) // 根据 ID 获取最大值
                .orElse(null); // 如果没有记录，返回 null

        if (maxIssue != null) {
            // 获取最大问题的编号
            String currentIssueNumber = maxIssue.getIssueNumber(); // 假设有一个方法 getIssueNumber()

            // 处理问题编号，取后四位并加1
            String lastFourDigits = currentIssueNumber.substring(currentIssueNumber.length() - 4);
            int newNumber = Integer.parseInt(lastFourDigits) + 1;

            // 返回格式化的编号（例如补零）
            return String.format("%04d", newNumber); // 修改这里返回字符串
        }

        return "0001"; // 如果理论上没有找到最大问题，返回默认值
    }



    @Autowired
    private IssueMaskTableDao issueMaskTableDao; // 新增的 DAO

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                new QueryWrapper<IssueTableEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPagecreator(Map<String, Object> params) {
        // 获取当前用户信息
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();

        // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        // 添加条件：查询创建人、rectificationResponsiblePerson 或 verifier 为 rolename 的数据
        queryWrapper.eq("creator", rolename)
                .or().eq("rectification_responsible_person", rolename)
                .or().eq("verifier", rolename);

        // 执行分页查询
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils QueryPage(Map<String, Object> params) {
        // 提取查询参数
        String issueCategoryId = (String) params.get("issueCategoryId");
        String vehicleTypeId = (String) params.get("vehicleTypeId");
        String responsibleDepartment = (String) params.get("responsibleDepartment");

        // 创建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        // 如果 issueCategoryId 不为空，则添加模糊查询条件
        if (issueCategoryId != null && !issueCategoryId.isEmpty()) {
            queryWrapper.like("issue_category_id", issueCategoryId);
        }

        // 如果 vehicleTypeId 不为空，则添加模糊查询条件
        if (vehicleTypeId != null && !vehicleTypeId.isEmpty()) {
            queryWrapper.like("vehicle_type_id", vehicleTypeId);
        }

        // 如果 responsibleDepartment 不为空，则添加模糊查询条件
        if (responsibleDepartment != null && !responsibleDepartment.isEmpty()) {
            queryWrapper.like("responsible_department", responsibleDepartment);
        }

        // 执行分页查询并返回结果
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils QueryFuPage(Map<String, Object> params) {
        // 提取查询参数
        String issueCategoryId = (String) params.get("issueCategoryId");
        // 创建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        // 如果 issueCategoryId 不为空，则添加模糊查询条件
        if (issueCategoryId != null && !issueCategoryId.isEmpty()) {
            queryWrapper.like("issue_category_id", issueCategoryId);
        }
        // 执行分页查询并返回结果
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                queryWrapper
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

//    @Override
//    public R closeRelatedTasks(Long issueId, Integer closeRelated) {
//        // 通过 ID 获取对应的实体类
//        IssueTableEntity issueTable = this.getById(issueId);
//        if (issueTable == null) {
//            return R.error("未找到该问题");
//        }
//
//        // 打印当前问题的基本信息
//        System.out.println("当前问题主键ID: " + issueTable.getIssueId());
//        System.out.println("当前问题编号: " + issueTable.getIssueNumber());
//
//        // 获取与当前问题相关的所有问题编号
//        String relatedIssueIdsString = issueTable.getAssociatedIssueAddition(); // 获取关联问题编号字符串
//        List<String> relatedIssueNumbers = parseRelatedIssueIds(relatedIssueIdsString); // 使用解析方法
//
//        // 打印拆分出的关联问题编号
//        if (!relatedIssueNumbers.isEmpty()) {
//            System.out.println("相关问题编号: " + String.join(", ", relatedIssueNumbers)); // 打印拆分后的关联问题编号
//        } else {
//            System.out.println("没有找到相关问题编号");
//        }
//
//        // 查找实际的ID
//        List<Long> relatedIssueIds = new ArrayList<>(); // 存储数据库中的问题ID
//        for (String number : relatedIssueNumbers) {
//            // 根据编号查找对应的ID
//            IssueTableEntity relatedIssue = this.list(Wrappers.<IssueTableEntity>lambdaQuery()
//                    .eq(IssueTableEntity::getIssueNumber, number)).stream().findFirst().orElse(null);
//            if (relatedIssue != null) {
//                relatedIssueIds.add(Long.valueOf(relatedIssue.getIssueId())); // 假设数据表中有这个字段
//                // 打印找到的关联问题的基本信息
//                System.out.println("找到相关问题的主键ID: " + relatedIssue.getIssueId() + ", 问题编号: " + relatedIssue.getIssueNumber());
//            } else {
//                System.out.println("未找到编号为 " + number + " 的相关问题");
//            }
//        }
//
//        // 打印将要删除的相关问题ID
//        if (!relatedIssueIds.isEmpty()) {
//            System.out.println("准备删除的关联问题ID: " + String.join(", ", relatedIssueIds.stream().map(String::valueOf).collect(Collectors.toList())));
//        } else {
//            System.out.println("没有要删除的关联问题ID");
//        }
//
//        try {
//            // 删除所有关联的问题ID
//            if (!relatedIssueIds.isEmpty()) {
//                this.removeByIds(relatedIssueIds); // 执行批量删除
//            }
//            // 删除当前问题
////            this.removeById(issueId);
////            System.out.println("当前问题ID " + issueId + " 已成功删除");
//
//            return R.ok().put("message", "相关任务和当前问题已成功关闭");
//        } catch (Exception e) {
//            e.printStackTrace(); // 输出异常信息
//            return R.error("关闭任务失败: " + e.getMessage());
//        }
//    }
@Override
public R closeRelatedTasks(Long issueId, Integer closeRelated) {
    // 通过 ID 获取对应的实体类
    IssueTableEntity issueTable = this.getById(issueId);
    if (issueTable == null) {
        return R.error("未找到该问题");
    }

    // 打印当前问题的基本信息
    System.out.println("当前问题主键ID: " + issueTable.getIssueId());
    System.out.println("当前问题编号: " + issueTable.getIssueNumber());

    // 获取与当前问题相关的所有问题编号
    String relatedIssueIdsString = issueTable.getAssociatedRectificationRecords(); // 获取关联问题编号字符串
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

    // 打印将要更新状态的相关问题ID
    if (!relatedIssueIds.isEmpty()) {
        System.out.println("准备更新状态的关联问题ID: " + String.join(", ", relatedIssueIds.stream().map(String::valueOf).collect(Collectors.toList())));
    } else {
        System.out.println("没有要更新状态的关联问题ID");
    }

    try {
        // 更新所有关联的问题状态为 "关闭"
        if (closeRelated == 1 && !relatedIssueIds.isEmpty()) {
            List<IssueTableEntity> relatedIssues = this.listByIds(relatedIssueIds);
            for (IssueTableEntity relatedIssue : relatedIssues) {
                relatedIssue.setState("关闭"); // 更新状态字段
            }
            this.updateBatchById(relatedIssues); // 批量更新
        }

        // 更新当前问题状态为 "关闭"
        issueTable.setState("关闭");
        this.updateById(issueTable);

        System.out.println("当前问题ID " + issueId + " 状态已成功更新为 '关闭'");

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

                // 读取初步分析
                issue.setPeliminaryAnalysis(getCellValueAsString(row.getCell(7)));

                // 读取问题描述
                issue.setIssueDescription(getCellValueAsString(row.getCell(8)));

                // 读取整改要求
                issue.setRectificationRequirement(getCellValueAsString(row.getCell(9)));

                // 读取要求完成时间
                Cell requiredCompletionTimeCell = row.getCell(10);
                if (requiredCompletionTimeCell != null && requiredCompletionTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(requiredCompletionTimeCell)) {
                    Date requiredCompletionTime = requiredCompletionTimeCell.getDateCellValue();
                    issue.setRequiredCompletionTime(requiredCompletionTime);
                } else {
                    issue.setRequiredCompletionTime(null);
                }

                // 读取责任部门
                issue.setResponsibleDepartment(getCellValueAsString(row.getCell(11)));

                // 读取整改状态
                issue.setRectificationStatus(getCellValueAsString(row.getCell(12)));

                // 读取实际完成时间
                Cell actualCompletionTimeCell = row.getCell(13);
                if (actualCompletionTimeCell != null && actualCompletionTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(actualCompletionTimeCell)) {
                    Date actualCompletionTime = actualCompletionTimeCell.getDateCellValue();
                    issue.setActualCompletionTime(actualCompletionTime);
                } else {
                    issue.setActualCompletionTime(null);
                }

                // 读取整改责任人
                issue.setRectificationResponsiblePerson(getCellValueAsString(row.getCell(14)));

                // 读取要求二次整改时间
//                Cell requiredSecondRectificationTimeCell = row.getCell(14);
//                if (requiredSecondRectificationTimeCell != null && requiredSecondRectificationTimeCell.getCellType() == CellType.NUMERIC
//                        && DateUtil.isCellDateFormatted(requiredSecondRectificationTimeCell)) {
//                    Date requiredSecondRectificationTime = requiredSecondRectificationTimeCell.getDateCellValue();
//                    issue.setRequiredSecondRectificationTime(requiredSecondRectificationTime);
//                } else {
//                    issue.setRequiredSecondRectificationTime(null);
//                }

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
//                issue.setLastModifier(getCellValueAsString(row.getCell(18)));

                // 读取最后修改时间
                Cell lastModificationTimeCell = row.getCell(18);
                if (lastModificationTimeCell != null && lastModificationTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(lastModificationTimeCell)) {
                    Date lastModificationTime = lastModificationTimeCell.getDateCellValue();
                    issue.setLastModificationTime(lastModificationTime);
                } else {
                    issue.setLastModificationTime(null);
                }

                // 读取关联整改记录
                issue.setAssociatedRectificationRecords(getCellValueAsString(row.getCell(19)));

                // 读取关联问题编号
//                issue.setAssociatedIssueAddition(getCellValueAsString(row.getCell(21)));

                // 读取实际完成时间
                Cell VerificationDeadline = row.getCell(20);
                if (VerificationDeadline != null && VerificationDeadline.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(VerificationDeadline)) {
                    Date actualCompletionTime = VerificationDeadline.getDateCellValue();
                    issue.setVerificationDeadline(actualCompletionTime);
                } else {
                    issue.setVerificationDeadline(null);
                }

                // 读取创建时长
                issue.setCreationDuration(getCellValueAsString(row.getCell(21)));

                // 读取原因分析
                issue.setCauseAnalysis(getCellValueAsString(row.getCell(22)));

                // 读取整改验证状态
                issue.setRectificationVerificationStatus(getCellValueAsString(row.getCell(23)));

                // 读取验证结论
                issue.setVerificationConclusion(getCellValueAsString(row.getCell(24)));

                // 读取验证人
                issue.setVerifier(getCellValueAsString(row.getCell(25)));

                // 读取公式
                issue.setFormula(getCellValueAsString(row.getCell(26)));

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

//    @Override
//    public Map<String, Integer> getCurrentMonthVerificationConclusionStatistics() {
//        Map<String, Integer> statistics = new HashMap<>();
//
//        // 定义可能的验证结论状态
//        List<String> verificationConclusions = Arrays.asList("暂缓", "结项");
//
//        // 获取当前月份起止日期
//        String currentMonthStart = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
//        LocalDate nextMonthFirstDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
//        String nextMonthFirstDayString = nextMonthFirstDay.format(DateTimeFormatter.ISO_DATE);
////        String currentMonthEnd = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
//
//        // 统计“创建”的数量（即 verification_conclusion 是 NULL 或空字符串的记录）
//        statistics.put("持续", countIssuesByCreationCondition(currentMonthStart, nextMonthFirstDayString));
//
//        // 对其他验证结论进行统计
//        for (String conclusion : verificationConclusions) {
//            statistics.put(conclusion, countIssuesByVerificationConclusion(conclusion, currentMonthStart, nextMonthFirstDayString));
//        }
//
//        return statistics;
//    }

    @Override
    public Map<String, Integer> getCurrentMonthVerificationConclusionStatistics() {
        Map<String, Integer> statistics = new HashMap<>();

        // 定义可能的验证结论状态
        List<String> verificationConclusions = Arrays.asList("暂缓", "结项");

        // 统计“创建”的数量（即 verification_conclusion 是 NULL 或空字符串的记录）
        statistics.put("持续", countIssuesByCreationCondition());

        // 对其他验证结论进行统计
        for (String conclusion : verificationConclusions) {
            statistics.put(conclusion, countIssuesByVerificationConclusion(conclusion));
        }

        return statistics;
    }


    @Override
    public IssueTableEntity getByissueNumber(String issueNumber) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("issue_number", issueNumber); // 根据问题编号查询

        // 调用 MyBatis-Plus 的方法查找对应的实体
        return this.getOne(queryWrapper, false); // 这里的 false 表示如果没有找到记录不会抛出异常
    }

    @Override
    public Map<String, Integer> getTaskDetails(String issueNumber) {
        // 初始化统计信息的 Map
        Map<String, Integer> stats = new HashMap<>();


        stats.put("totalCount", 0);
        stats.put("completedCount", 0);
        stats.put("inProgressCount", 0);
        stats.put("reviewingCount", 0);

        // 创建 QueryWrapper 并配置查询条件
        QueryWrapper<IssueMaskTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("issue_number", issueNumber); // 根据 issueNumber 进行筛选
        // 查询与 issueNumber 匹配的所有任务实体
        List<IssueMaskTableEntity> taskList = issueMaskTableDao.selectList(queryWrapper); // 通过 issueMaskTableDao 查询




        // 遍历任务列表，根据状态进行分类统计
        for (IssueMaskTableEntity task : taskList) {
            switch (task.getState()) {
                case "已完成":
                    stats.put("completedCount", stats.get("completedCount") + 1);
                    break;
                case "执行中":
                    stats.put("inProgressCount", stats.get("inProgressCount") + 1);
                    break;
                case "审核中":
                    stats.put("reviewingCount", stats.get("reviewingCount") + 1);
                    break;
                default:
                    break;
            }
        }

        // 计算总任务数量
        int totalCount = stats.get("completedCount") + stats.get("inProgressCount") + stats.get("reviewingCount");
        stats.put("totalCount", totalCount); // 更新总任务数量

        // 返回统计信息
        return stats;

    }

    @Override
    public void connectionIssue(Long issueId) {
        // 1. 从数据库中获取传入的 issueId 对应的实体
        IssueTableEntity currentIssue = this.getById(issueId);
        System.out.println("获取的当前问题: " + currentIssue); // 打印当前问题实体

        if (currentIssue == null) {
            throw new IllegalArgumentException("未找到该问题");
        }

        // 2. 获取传入问题的 issueCategoryId 和 issueNumber
        String issueCategoryId = currentIssue.getIssueCategoryId();
        String issueNumber = currentIssue.getIssueNumber();
        System.out.println("当前问题的 issueCategoryId: " + issueCategoryId); // 打印问题分类ID
        System.out.println("当前问题的 issueNumber: " + issueNumber); // 打印问题编号

        // 3. 查询所有具有相同 issueCategoryId 的记录
        List<IssueTableEntity> relatedIssues = this.list(
                Wrappers.<IssueTableEntity>lambdaQuery()
                        .eq(IssueTableEntity::getIssueCategoryId, issueCategoryId)
        );
        System.out.println("找到的相关问题数量: " + relatedIssues.size()); // 打印找到的相关问题数量

        // 4. 创建用于存储所有相同类型问题的车号的集合
        List<String> relatedVehicleNumbers = new ArrayList<>();
        StringBuilder associatedRectificationRecordsBuilder = new StringBuilder();

        // 5. 遍历所有相关问题，处理其 associatedRectificationRecords 字段
        for (IssueTableEntity issue : relatedIssues) {
            String associatedRecords = issue.getAssociatedRectificationRecords();
            System.out.println("问题关联记录: " + associatedRecords + " + " ); // 打印更新过程
            // 如果 records 不为空，前面加上逗号，并添加问题编号
            if (associatedRecords != null && !associatedRecords.isEmpty()) {
                associatedRectificationRecordsBuilder.append(",").append(issueNumber);
                String newAssociatedRecords = associatedRecords + associatedRectificationRecordsBuilder;
//                System.out.println("associatedRectificationRecordsBuilder1: " + associatedRectificationRecordsBuilder); // 打印更新过程
//                System.out.println("associatedRectificationRecordsBuilder1: " + newAssociatedRecords); // 打印更新过程
                issue.setAssociatedRectificationRecords(newAssociatedRecords);
                System.out.println("更新相关问题的关联整改记录: " + associatedRecords + " + " + issueNumber); // 打印更新过程
                boolean relatedUpdateSuccess = this.updateById(issue);
            }
            else {
                System.out.println("无记录的空问题处理: " + associatedRecords + " + " + issueNumber); // 打印更新过程
                issue.setAssociatedRectificationRecords(issueNumber);
                boolean relatedUpdateSuccess = this.updateById(issue);
            }
            // 收集车号
            String vehicleNumber = issue.getIssueNumber(); //
            if (vehicleNumber != null && !vehicleNumber.isEmpty()) {
                relatedVehicleNumbers.add(vehicleNumber);
            }
        }

        // 6. 将当前问题的 associatedRectificationRecords 修改为收集到的所有车号
        String newAssociatedRecords = String.join(",", relatedVehicleNumbers);
        currentIssue.setAssociatedRectificationRecords(newAssociatedRecords);
//        System.out.println("更新当前问题的 associatedRectificationRecords: " + currentIssue); // 打印更新后的记录

        // 7. 更新当前问题的实体
        boolean updateSuccess = this.updateById(currentIssue);
//        System.out.println("当前问题更新操作成功: " + updateSuccess); // 打印更新操作结果

//        // 8. 更新所有相关问题的 associatedRectificationRecords
//        for (IssueTableEntity issue : relatedIssues) {
//            String previousRecords = issue.getAssociatedRectificationRecords();
//            if (previousRecords != null && !previousRecords.isEmpty()) {
//                issue.setAssociatedRectificationRecords(previousRecords + associatedRectificationRecordsBuilder.toString());
//                System.out.println("更新相关问题ID: " + issue.getIssueId() + " 的关联整改记录: " + issue.getAssociatedRectificationRecords()); // 打印相关问题的更新
//            } else {
//                // 如果之前没有记录就直接设置
//                issue.setAssociatedRectificationRecords(associatedRectificationRecordsBuilder.toString().replaceFirst(",", ""));
//                System.out.println("新增相关问题ID: " + issue.getIssueId() + " 的关联整改记录: " + issue.getAssociatedRectificationRecords()); // 打印相关问题的新增
//            }
//            // 更新每个相关问题
//            boolean relatedUpdateSuccess = this.updateById(issue);
//            System.out.println("相关问题ID: " + issue.getIssueId() + " 更新操作成功: " + relatedUpdateSuccess); // 打印每个相关问题的更新结果
//        }
        // 8. 更新所有相关问题的 associatedRectificationRecords
//        for (IssueTableEntity issue : relatedIssues) {
//            // 将每个相关问题的 associatedRectificationRecords 修改为 newAssociatedRecords
//            issue.setAssociatedRectificationRecords(newAssociatedRecords);
//            System.out.println("更新相关问题ID: " + issue.getIssueId() + " 的关联整改记录为: " + newAssociatedRecords); // 打印更新后的记录
//
//            // 更新每个相关问题
//            boolean relatedUpdateSuccess = this.updateById(issue);
//            System.out.println("相关问题ID: " + issue.getIssueId() + " 更新操作成功: " + relatedUpdateSuccess); // 打印每个相关问题的更新结果
//        }

    }

    @Override
    public Workbook generateTemplate() {
        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("问题表模板");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        Field[] fields = IssueTableEntity.class.getDeclaredFields();
        int cellIndex = 0;

        // 定义字段名和中文描述的对应关系
        Map<String, String> fieldDescriptionMap = new HashMap<>();
        fieldDescriptionMap.put("serialNumber", "序号");
        fieldDescriptionMap.put("issueNumber", "问题编号");
        fieldDescriptionMap.put("inspectionDepartment", "检查科室");
        fieldDescriptionMap.put("inspectionDate", "检查日期");
        fieldDescriptionMap.put("issueCategoryId", "问题类别");
        fieldDescriptionMap.put("vehicleTypeId", "车型");
        fieldDescriptionMap.put("vehicleNumberId", "车号");
        fieldDescriptionMap.put("peliminaryAnalysis", "初步要求");
        fieldDescriptionMap.put("issueDescription", "问题描述");
        fieldDescriptionMap.put("rectificationRequirement", "整改要求");
        fieldDescriptionMap.put("requiredCompletionTime", "要求完成时间");
        fieldDescriptionMap.put("responsibleDepartment", "责任科室");
        fieldDescriptionMap.put("rectificationStatus", "整改情况");
        fieldDescriptionMap.put("actualCompletionTime", "实际完成时间");
        fieldDescriptionMap.put("rectificationResponsiblePerson", "整改责任人");
//        fieldDescriptionMap.put("requiredSecondRectificationTime", "要求二次整改时间");
        fieldDescriptionMap.put("remark", "备注");
        fieldDescriptionMap.put("creator", "创建人");
        fieldDescriptionMap.put("creationTime", "创建时间");
//        fieldDescriptionMap.put("lastModifier", "最后修改人");
        fieldDescriptionMap.put("lastModificationTime", "最后修改时间");
        fieldDescriptionMap.put("associatedRectificationRecords", "关联问题整改记录");
//        fieldDescriptionMap.put("associatedIssueAddition", "关联问题添加");
        fieldDescriptionMap.put("verificationDeadline", "要求二次整改时间");
        fieldDescriptionMap.put("creationDuration", "整改时长");
        fieldDescriptionMap.put("causeAnalysis", "原因分析");
        fieldDescriptionMap.put("rectificationVerificationStatus", "整改验证情况");
        fieldDescriptionMap.put("verificationConclusion", "验证结论");
        fieldDescriptionMap.put("verifier", "验证人");
        fieldDescriptionMap.put("formula", "公式");

        // 只写入需要的字段
        for (Field field : fields) {
            String fieldName = field.getName();

            // 跳过不需要的字段
            if (fieldName.equals("serialVersionUID") ||
                    fieldName.equals("issueId") ||
                    fieldName.equals("indicatorId") ||
                    fieldName.equals("issuePhoto") ||
                    fieldName.equals("rectificationPhotoDeliverable") ||
                    fieldName.equals("reviewers") ||
                    fieldName.equals("level") ||
                    fieldName.equals("state")
//                    ||
//                    fieldName.equals("formula")
            ) {
                continue; // 不写入这些字段的标题
            }

            String columnTitle = fieldDescriptionMap.getOrDefault(fieldName, fieldName); // 获取中文标题
            // 创建表头单元格
            var cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(columnTitle); // 设置中文标题
        }

        return workbook; // 返回生成的工作簿
    }

    @Override
    public void removeAll(Integer[] issueIds) {
        // 1. 查询所有关联的问题编号
        List<String> issueNumbers = new ArrayList<>();

        // 根据 issueIds 获取所有对应的 IssueTableEntity
        List<IssueTableEntity> issues = this.listByIds(Arrays.asList(issueIds));
        for (IssueTableEntity issue : issues) {
            issueNumbers.add(issue.getIssueNumber());
        }
        // 2. 根据问题编号查找任务表
        // 创建查询条件
        QueryWrapper<IssueMaskTableEntity> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.in("issue_number", issueNumbers); // 根据问题编号查询

        // 获取任务列表
        List<IssueMaskTableEntity> tasksToDelete = issueMaskTableDao.selectList(taskQueryWrapper);
        // 3. 删除任务
        if (!tasksToDelete.isEmpty()) {
            List<Integer> taskIdsToDelete = tasksToDelete.stream()  // 将 List<Long> 修改为 List<Integer>
                    .map(IssueMaskTableEntity::getIssuemaskId) // 使用 getIssuemaskId() 方法获取任务 ID
                    .collect(Collectors.toList());
            issueMaskTableDao.deleteBatchIds(taskIdsToDelete); // 批量删除任务
            System.out.println("已成功删除任务 ID: " + taskIdsToDelete);
        } else {
            System.out.println("没有找到与给定问题编号关联的任务");
        }


    }

//    @Override
//    public Map<String, Integer> getCurrentMonthCompletionRate() {
//        // 获取当前月的起始和结束日期
//        LocalDate now = LocalDate.now();
//        String startDate = now.withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE); // 本月第一天
//        String endDate = now.plusMonths(1).withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE); // 下月第一天
//
//        // 统计当前月份已完成和未完成的条数
//        int completedCount = countIssuesByVerificationConclusion("已完成", startDate, endDate);
//        int notCompletedCount = countIssuesByVerificationConclusion("未完成", startDate, endDate);
//        int pauseCount = countIssuesByVerificationConclusion("暂停", startDate, endDate);
//        int conclusionCount = countIssuesByVerificationConclusion("结项", startDate, endDate);
//        int profoundCount1 = countIssuesByVerificationConclusion("",startDate,endDate);
////        int profoundCount2 = countIssuesByVerificationConclusion(null,startDate,endDate);
//        int tolCompleted = notCompletedCount + pauseCount + conclusionCount + profoundCount1 ;
//        // 创建一个结果Map用于存放完成率数据
//        Map<String, Integer> completionRate = new HashMap<>();
//        completionRate.put("completed", completedCount);
//        completionRate.put("tolCompleted", tolCompleted);
//
//        return completionRate;
//    }
@Override
public Map<String, Integer> getCurrentMonthCompletionRate() {
    // 统计已完成、未完成、暂停、结项等状态的条数
    int completedCount = countIssuesByVerificationConclusion("结项");
    int notCompletedCount = countIssuesByVerificationConclusion("持续");
    int pauseCount = countIssuesByVerificationConclusion("暂缓");
//    int conclusionCount = countIssuesByVerificationConclusion("结项");
    int profoundCount1 = countIssuesByVerificationConclusion("");  // 空字符串表示没有结论的情况

    // 计算总的已完成条数
    int tolCompleted = countAllIssues();  // 查询所有记录的总数

    // 创建一个结果Map用于存放完成率数据
    Map<String, Integer> completionRate = new HashMap<>();
    completionRate.put("completed", completedCount);
    completionRate.put("tolCompleted", tolCompleted);

    return completionRate;
}

    @Override
    public boolean checkDuplicateIssue(List<String> vehicleNumbers, String issueCategoryIds) {
        // 1. 查询与问题类别相同的所有记录
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("issue_category_id", issueCategoryIds);

        List<IssueTableEntity> issueList = this.list(queryWrapper); // 获取所有符合条件的记录
        Set<String> vehicleNumbersFromDb = new HashSet<>();  // 用于存储从数据库中提取的车号

        // 2. 处理每条记录的 vehicle_number_id 字段，按逗号分割并保存到集合中
        for (IssueTableEntity issue : issueList) {
            String vehicleNumberId = issue.getVehicleNumberId();  // 获取车号字段
            if (vehicleNumberId != null) {
                String[] vehicleNumberArray = vehicleNumberId.split(","); // 按逗号分隔车号
                for (String vehicleNumber : vehicleNumberArray) {
                    vehicleNumbersFromDb.add(vehicleNumber.trim());  // 添加到集合中，去除前后空格
                }
            }
        }

        System.out.println("从数据库中提取的车号: " + vehicleNumbersFromDb);

        // 3. 用传入的车号列表与从数据库中提取的车号进行匹配
        int count = 0;
        for (String vehicleNumber : vehicleNumbers) {
            if (vehicleNumbersFromDb.contains(vehicleNumber)) {
                count++; // 如果传入的车号在数据库中存在，计数加一
            }
        }

        System.out.println("匹配到的车号数量: " + count);
        // 如果有至少一个车号匹配，则返回 true
        return count > 0;
    }






    @Override
    public IssueTableEntity getByassociate(String associatedRectificationRecords) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
//        System.out.println("associatedRectificationRecords: " + associatedRectificationRecords);
        queryWrapper.eq("issue_number", associatedRectificationRecords); // 根据问题编号查询

        // 调用 MyBatis-Plus 的方法查找对应的实体
        return this.getOne(queryWrapper, false); // 这里的 false 表示如果没有找到记录不会抛出异常
    }

    @Override
    public boolean checkReplicateIssue(Integer issueId, String systematicClassification, String firstFaultyParts, String secondFaultyParts, String faultType, String faultModel) {
                // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("Systematic_classification",systematicClassification )
                    .eq("fault_type", faultType)
                    .eq("First_Faulty_parts", firstFaultyParts)
                    .eq("Second_Faulty_parts", secondFaultyParts)
                    .eq("fault_model",faultModel );



        // 查询符合条件的记录
        List<IssueTableEntity> issues = this.list(queryWrapper);

        // 判断是否存在重复记录
        boolean isDuplicate = !issues.isEmpty();

        // 更新 overdue 属性为 true
        for (IssueTableEntity issue : issues) {
            issue.setOverDue("true");
            this.updateById(issue);
        }

        return isDuplicate; // 返回是否存在重复记录
    }





    private Integer countAllIssues() {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        // 查询所有记录
        return this.count(queryWrapper);
    }




    // 统计“创建”的条件
//    private Integer countIssuesByCreationCondition(String startDate, String endDate) {
//        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.and(wrapper ->
//                        wrapper.isNull("verification_conclusion")
//                                .or().eq("verification_conclusion", "")
//                                .or().like("verification_conclusion", "持续")
//                )
//                .ge("creation_time", startDate)
//                .le("creation_time", endDate);
//
//        return this.count(queryWrapper);
//    }
    private Integer countIssuesByCreationCondition() {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                wrapper.isNull("verification_conclusion")
                        .or().eq("verification_conclusion", "")
                        .or().like("verification_conclusion", "持续")
        );

        return this.count(queryWrapper);
    }


    // 根据 verification_conclusion 统计数量，处理包含多个状态的组合
//    private Integer countIssuesByVerificationConclusion(String conclusion, String startDate, String endDate) {
//        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
//
//        // 使用 FIND_IN_SET() 函数判断字段中是否包含该状态
//        queryWrapper.ge("creation_time", startDate)
//                .le("creation_time", endDate)
//                .like("verification_conclusion", conclusion); // 使用 LIKE 查询
//
//        return this.count(queryWrapper);
//    }
    private Integer countIssuesByVerificationConclusion(String conclusion) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("verification_conclusion", conclusion); // 使用 LIKE 查询

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

    private Integer countIssuesByStateAndDate(String state, String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("verification_conclusion", state) // 状态匹配
                .ge("creation_time", startDate) // 创建时间在本月
                .lt("creation_time", endDate); // 创建时间在本月内

        return this.count(queryWrapper); // 使用 MyBatis-Plus 的 count 方法
    }




}
