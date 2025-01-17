package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import io.renren.modules.indicator.entity.IndicatorKeyIndicatorsEntity;
import io.renren.modules.qcManagement.entity.QcknowledgebaseEntity;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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
        Page<IssueTableEntity> page = new Page<>(p, l);
        String creationTime = (String) params.get("creationTime");
        String issueDescription = (String) params.get("issueDescription");
        try {
            // 执行分页查询
            List<IssueTableEntity> result = issueTableDao.selectFinishedSubjectList(creationTime,issueDescription);

            page.setRecords(result);
            page.setTotal(result.size());

            log.info("result" + page);
            return new PageUtils(page);
        } catch (Exception e) {
            log.error("查询出错: " + e.getMessage() + ", params: " + params, e);
            page.setTotal(0);
            return new PageUtils(page);
        }
    }

//    @Override
//    public PageUtils queryPageFinishedList(Map<String, Object> params) {
//        log.info("param" + params.get("page") + "------" + params.get("limit"));
//
//        long p = Long.parseLong((String) params.get("page"));
//        long l = Long.parseLong((String) params.get("limit"));
//        Page<QcknowledgebaseEntity> page = new Page<>(p, l);
//
//        // 提取模糊查询参数
//        String topicName = (String) params.get("topicName");
//        String keywords = (String) params.get("keywords");
//        String startDate = (String) params.get("startDate");
//        String endDate = (String) params.get("endDate");
//
//        try {
//            // 执行分页查询
//            List<QcknowledgebaseEntity> result = qcSubjectRegistrationDao.selectFinishedSubjectList(topicName, keywords,startDate,endDate);
//            page.setRecords(result);
//            page.setTotal(result.size());
//
//            log.info("result" + page);
//            return new PageUtils(page);
//        } catch (Exception e) {
//            log.error("查询出错: " + e.getMessage() + ", params: " + params, e);
//            page.setTotal(0);
//            return new PageUtils(page);
//        }
//    }

//    @Override
//    public String newIssueNumber() {
//        // 查询所有的问题
//        List<IssueTableEntity> issues = this.list();
//
//        // 如果数据库为空，返回默认值 "0001"
//        if (issues.isEmpty()) {
//            return "0001";
//        }
//
//        // 找到 ID 最大的问题
//        IssueTableEntity maxIssue = issues.stream()
//                .max(Comparator.comparingLong(IssueTableEntity::getIssueId)) // 根据 ID 获取最大值
//                .orElse(null); // 如果没有记录，返回 null
//
//        if (maxIssue != null) {
//            // 获取最大问题的编号
//            String currentIssueNumber = maxIssue.getIssueNumber(); // 假设有一个方法 getIssueNumber()
//
//            // 处理问题编号，取后四位并加1
//            String lastFourDigits = currentIssueNumber.substring(currentIssueNumber.length() - 4);
//            int newNumber = Integer.parseInt(lastFourDigits) + 1;
//
//            // 返回格式化的编号（例如补零）
//            return String.format("%04d", newNumber); // 修改这里返回字符串
//        }
//
//        return "0001"; // 如果理论上没有找到最大问题，返回默认值
//    }

    @Override
    public String newIssueNumber() {
        // 查询所有问题编号的后四位
        List<IssueTableEntity> issues = this.list();
        if (issues.isEmpty()) {
            return "0001"; // 如果没有记录，返回 "0001"
        }

        // 获取所有的后四位编号，过滤掉非基础编号
        List<String> lastFourDigitsList = issues.stream()
                .map(IssueTableEntity::getIssueNumber)
                .filter(issueNumber -> issueNumber.matches("ZL-IS-\\d{8}-\\d{4}")) // 只匹配基础编号
                .map(issueNumber -> issueNumber.substring(issueNumber.lastIndexOf("-") + 1)) // 提取后四位
                .collect(Collectors.toList());

        if (lastFourDigitsList.isEmpty()) {
            return "0001"; // 如果没有有效的后四位，返回 "0001"
        }

        // 找到后四位最大的编号
        String maxLastFourDigits = lastFourDigitsList.stream()
                .max(Comparator.comparingInt(Integer::parseInt)) // 获取最大值
                .orElse(null);

        if (maxLastFourDigits != null) {
            // 递增最大值的后四位
            int newNumber = Integer.parseInt(maxLastFourDigits) + 1;

            // 返回新的后四位编号，确保编号是四位数
            return String.format("%04d", newNumber);
        }

        // 如果没有有效编号，返回默认值
        return "0001";
    }




    @Override
    public String connectNumber(Integer id) {
        // 步骤 1：根据 ID 查询数据库，获取 issueNumber
        IssueTableEntity issue = issueTableDao.selectById(id);
        if (issue == null) {
            throw new RuntimeException("Issue not found");
        }
        // 步骤 3：处理查询结果
        String issueNumber = issue.getParentQuestion();
        if (issueNumber == null || issueNumber == "") {
            return "1";  // 没有查询到记录，返回 1
        }
        else {
        // 步骤 2：使用 MyBatis-Plus 查询所有记录，检查 parentQuestion 是否与 issueNumber 匹配
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_Question", issueNumber);  // 使用 QueryWrapper 来查找 parentQuestion 等于 issueNumber 的记录
        List<IssueTableEntity> matchingIssues = issueTableDao.selectList(queryWrapper);
            return String.valueOf(matchingIssues.size()+1);  // 查询到记录，返回记录数
        }
    }


    /**
     * 获取按年份和月份统计的问题数
     * @param year 年份
     * @return 按月份统计的问题数
     */
    @Override
    public Map<String, Integer> getMonthlyCountByYear(int year) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MONTH(creation_time) AS month", "COUNT(*) AS count")
                .eq("YEAR(creation_time)", year) // 筛选出指定年份的数据
                .groupBy("MONTH(creation_time)") // 按月分组
                .orderByAsc("month"); // 按月份排序

        // 执行查询，获取每月的问题数量
        List<Map<String, Object>> result = issueTableDao.selectMaps(queryWrapper);

        // 将查询结果转换为 Map<String, Integer> 格式，月份作为 key，数量作为 value
        Map<String, Integer> monthlyStats = new HashMap<>();

        // 初始化 Map，确保所有月份都有数据
        for (int i = 1; i <= 12; i++) {
            monthlyStats.put(String.valueOf(i), 0);  // 初始化为 0
        }

        // 遍历查询结果，将统计值填充到 Map 中
        for (Map<String, Object> stat : result) {
            String month = stat.get("month").toString(); // 获取月份
            Integer count = Integer.parseInt(stat.get("count").toString()); // 获取数量
            monthlyStats.put(month, count);
        }

        return monthlyStats;
    }

    /**
     * 获取按月份统计的重复问题总数（按年份）
     * @param year 年份
     * @return 返回统计数据
     */
    @Override
    public Map<String, Integer> getmonthlyDuplicateStats(int year) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MONTH(creation_time) AS month", "COUNT(*) AS count")
                .eq("YEAR(creation_time)", year) // 筛选出指定年份的数据
                .eq("over_due", "true") // 筛选 out over_due 为 "true" 的记录重复问题
                .isNull("parent_Question").or().eq("parent_Question", "")
                .groupBy("MONTH(creation_time)") // 按月分组
                .orderByAsc("month"); // 按月份排序

        // 执行查询，获取每月的问题数量
        List<Map<String, Object>> result = issueTableDao.selectMaps(queryWrapper);

        // 将查询结果转换为 Map<String, Integer> 格式，月份作为 key，数量作为 value
        Map<String, Integer> monthlyStats = new HashMap<>();

        // 初始化 Map，确保所有月份都有数据
        for (int i = 1; i <= 12; i++) {
            monthlyStats.put(String.valueOf(i), 0);  // 初始化为 0
        }

        // 遍历查询结果，将统计值填充到 Map 中
        for (Map<String, Object> stat : result) {
            String month = stat.get("month").toString(); // 获取月份
            Integer count = Integer.parseInt(stat.get("count").toString()); // 获取数量
            monthlyStats.put(month, count);
        }

        return monthlyStats;
    }




    @Autowired
    private IssueMaskTableDao issueMaskTableDao; // 新增的 DAO



    @Override
    public PageUtils queryPageByDescription(Map<String, Object> params, String description) {//修改
        // 获取分页参数
        long p = Long.parseLong((String) params.get("page"));
        long l = Long.parseLong((String) params.get("limit"));
        Page<IssueTableEntity> page = new Page<>(p, l);

        // 调用 DAO 层方法进行分页查询，传递问题描述  问题描述
        Page<IssueTableEntity> result = issueTableDao.selectFinishedSubjectListByDescription(page, description);

        return new PageUtils(result);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        // 按 creation_time 降序排序
        queryWrapper.orderByDesc("creation_time");

        // 执行分页查询
        IPage<IssueTableEntity> page = this.page(
                new Query<IssueTableEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPagecreator(Map<String, Object> params) {
        // 获取当前用户信息
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = String.valueOf(role.getUserId());

        // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        // 添加条件：查询创建人、rectificationResponsiblePerson 或 verifier 为 rolename 的数据
        queryWrapper.eq("creator", rolename)
                .or().eq("rectification_responsible_person", rolename)
                .or().eq("verifier", rolename);

        // 按 ID 降序排序
        queryWrapper.orderByDesc("creation_time");

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
//        System.out.println("///" + list());
        return this.list();
    }
    @Override
    public List<IssueTableEntity> listAll01(Map<String, Object> params) {
        log.info("param: page=" + params.get("page") + ", limit=" + params.get("limit"));

        String creationTime = (String) params.get("creationTime");
        String issueDescription = (String) params.get("issueDescription");

        try {
            // 执行查询
            List<IssueTableEntity> result = issueTableDao.selectFinishedSubjectList(creationTime, issueDescription);

            log.info("Query result size: " + result.size());
            return result;
        } catch (Exception e) {
            log.error("查询出错: " + e.getMessage() + ", params: " + params, e);
            return new ArrayList<>(); // 返回空列表以避免调用方出错
        }
    }

    //问题知识库删除功能
    public boolean clearStorageFlag(List<Integer> ids) {
        List<IssueTableEntity> list = this.list(new LambdaQueryWrapper<IssueTableEntity>().in(IssueTableEntity::getIssueId, ids));
        for (IssueTableEntity issueTableEntity : list) {
            issueTableEntity.setStorageFlag(0);
        }
        this.saveOrUpdateBatch(list);
        return true;
    }

    @Override
    public int getallissue() {
        // 获取当前日期和本月的第一天、最后一天
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("creation_time", startOfMonth, endOfMonth)
                .isNull("parent_Question").or().eq("parent_Question", "");

        // 执行查询返回记录总数
        return this.count(queryWrapper);
    }

    @Override
    public Map<String, Object> getThirdIssue() {

        Map<String, Object> statistics = new HashMap<>();

        // 1. 计算超期的总数和累计超期时间
        QueryWrapper<IssueTableEntity> overdueWrapper = new QueryWrapper<>();
        overdueWrapper.isNotNull("required_completion_time").isNotNull("actual_completion_time");
        List<IssueTableEntity> issues = this.list(overdueWrapper);

        int overdueCount = 0;
        double totalOverdueTime = 0;

        for (IssueTableEntity issue : issues) {
            LocalDateTime requiredCompletionTime = issue.getRequiredCompletionTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime actualCompletionTime = issue.getActualCompletionTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            if (actualCompletionTime.isAfter(requiredCompletionTime)) {
                overdueCount++;

                // 精确计算时间差，以小时为单位
                long overdueMilliseconds = Duration.between(requiredCompletionTime, actualCompletionTime).toMillis();
                System.out.println("overdueMilliseconds: " + overdueMilliseconds);
                double overdueDays = overdueMilliseconds / (1000.0 * 60 * 60 * 24); // 转换为天数（带小数）
                System.out.println("overdueDays: " + overdueDays);
                totalOverdueTime += overdueDays;
            }

        }
        statistics.put("超期总数", overdueCount);
        // 格式化为小数点后两位
        statistics.put("累计超期时间", String.format("%.2f", totalOverdueTime));

        // 2. 统计 "等待整改记录填写" 的数据条数以及总数据条数
        int pendingRectificationCount = countIssuesBylevel("等待整改记录填写");
        int totalIssuesCount = this.count(); // 统计所有记录的总数

        statistics.put("等待整改记录填写数量", pendingRectificationCount);
        statistics.put("总数据条数", totalIssuesCount);

        // 3. 统计 "等待验证"、"验证未通过"、"结项" 的数据条数
        int waitingForVerification = countIssuesBylevel("等待验证");
        int verificationFailed = countIssuesBylevel("未通过验证");
        int completedIssues = countIssuesBylevel("结项");

        statistics.put("等待验证数量", waitingForVerification);
        statistics.put("验证未通过数量", verificationFailed);
        statistics.put("结项数量", completedIssues);

        System.out.println("第三部分统计数据"+statistics+"++++++ ");

        return statistics;
    }

    @Override
    public Map<String, Map<String, Integer>> getIssuebyDepartment(String startDate, String endDate) {
        // 创建统计结果的容器
        Map<String, Map<String, Integer>> departmentStatistics = new HashMap<>();

        // 获取指定时间范围内的所有记录的责任科室和完成状态
        List<IssueTableEntity> issueList = getIssuesByDateRange(startDate, endDate);

        // 遍历记录，统计每个责任科室的问题总数和已完成问题总数
        for (IssueTableEntity issue : issueList) {
            String department = issue.getResponsibleDepartment(); // 责任科室
            String state = issue.getState(); // 完成情况

            // 初始化科室统计数据
            departmentStatistics.putIfAbsent(department, new HashMap<>());
            Map<String, Integer> completionStats = departmentStatistics.get(department);

            completionStats.putIfAbsent("total", 0);       // 默认值为 0
            completionStats.putIfAbsent("completed", 0);  // 默认值为 0

            // 更新问题总数
            completionStats.put("total", completionStats.getOrDefault("total", 0) + 1);

            // 更新已完成问题数
            if ("已完成".equals(state)) {
                completionStats.put("completed", completionStats.getOrDefault("completed", 0) + 1);
            }
        }
//        System.out.println("wancehngqingkuang" + departmentStatistics +"------------");

        return departmentStatistics;
    }

    @Override
    public Map<String, Map<String, Object>> getbyindemnification(String startDate, String endDate) {
        // 定义结果集：每种赔偿件类型对应一个 Map，包含发生次数和成本总和
        Map<String, Map<String, Object>> indemnificationTypeStatistics = new HashMap<>();

        // 获取当前月份所有赔偿件数据
        List<IssueTableEntity> indemnificationIssues = getIndemnificationIssues(startDate, endDate);

        // 遍历赔偿件数据，统计每种赔偿件的发生次数和成本总和
        for (IssueTableEntity issue : indemnificationIssues) {
            String indemnificationType = issue.getIndemnification(); // 获取赔偿件类型
            double qualityCost = issue.getQualitycost(); // 获取成本字段

            // 如果当前赔偿件类型不存在，则初始化
            // 初始化赔偿件统计数据
            indemnificationTypeStatistics.putIfAbsent(indemnificationType, new HashMap<>());
            Map<String, Object> typeStats = indemnificationTypeStatistics.get(indemnificationType);

            // 初始化字段 "count" 和 "totalCost"，默认为 0
            typeStats.putIfAbsent("count", 0);       // 初始化次数为 0
            typeStats.putIfAbsent("totalCost", 0.0); // 初始化成本总和为 0.0

            // 更新赔偿件的发生次数和成本总和
            typeStats.put("count", (int) typeStats.get("count") + 1); // 次数加 1
            typeStats.put("totalCost", (double) typeStats.get("totalCost") + qualityCost); // 成本累加
        }
        System.out.println("赔偿将统计数量" + indemnificationTypeStatistics + "+++++");
        return indemnificationTypeStatistics;
    }

    @Override
    public Map<String, Map<String, Object>> getbyvendor(String startDate, String endDate) {
        // 定义结果集：每种赔偿件类型对应一个 Map，包含发生次数和成本总和
        Map<String, Map<String, Object>> venderTypeStatistics = new HashMap<>();

        // 获取当前月份所有赔偿件数据
        List<IssueTableEntity> venderIssues = getvenderIssues(startDate, endDate);

        // 遍历赔偿件数据，统计每种赔偿件的发生次数和成本总和
        for (IssueTableEntity issue : venderIssues) {
            String venderType = issue.getVendor(); // 获取赔偿件类型
            double qualityCost = issue.getQualitycost(); // 获取成本字段

            // 如果当前供应商不存在，则初始化
            // 初始化供应商统计数据
            venderTypeStatistics.putIfAbsent(venderType, new HashMap<>());
            Map<String, Object> typeStats = venderTypeStatistics.get(venderType);

            // 初始化字段 "count" 和 "totalCost"，默认为 0
            typeStats.putIfAbsent("count", 0);       // 初始化次数为 0
            typeStats.putIfAbsent("totalCost", 0.0); // 初始化成本总和为 0.0

            // 更新供应商的发生次数和成本总和
            typeStats.put("count", (int) typeStats.get("count") + 1); // 次数加 1
            typeStats.put("totalCost", (double) typeStats.get("totalCost") + qualityCost); // 成本累加
        }
        System.out.println("赔偿将统计数量" + venderTypeStatistics + "+++++");
        return venderTypeStatistics;
    }

    @Override
    public Map<String, Map<String, Object>> getbyvehicletype(String startDate, String endDate) {
        // 定义结果集：每种车型对应一个 Map，包含发生次数和成本总和
        Map<String, Map<String, Object>> carModelStatistics = new HashMap<>();

        // 获取当前月份所有问题的车型数据
        List<IssueTableEntity> issueList = getIssuesByCarModel(startDate, endDate);

        // 遍历问题数据，统计每种车型的发生次数和生成成本
        for (IssueTableEntity issue : issueList) {
            String vehicleTypeIds = issue.getVehicleTypeId(); // 获取车型字段（可能是多个车型，例如 "A,B,C"）
            double generationCost = issue.getQualitycost(); // 获取生成成本字段

            // 拆分车型数据，处理每种车型
            String[] vehicleTypes = vehicleTypeIds.split(",");
            for (String vehicleType : vehicleTypes) {
                vehicleType = vehicleType.trim(); // 去除多余的空格

                // 如果当前车型不存在，则初始化
                carModelStatistics.putIfAbsent(vehicleType, new HashMap<>());
                Map<String, Object> typeStats = carModelStatistics.get(vehicleType);

                // 初始化字段 "count" 和 "totalCost"，默认为 0
                typeStats.putIfAbsent("count", 0);       // 初始化次数为 0
                typeStats.putIfAbsent("totalCost", 0.0); // 初始化成本总和为 0.0

                // 更新车型的发生次数和生成成本总和
                typeStats.put("count", (int) typeStats.get("count") + 1); // 次数加 1
                typeStats.put("totalCost", (double) typeStats.get("totalCost") + generationCost); // 成本累加
            }
        }

        System.out.println("车型统计数据：" + carModelStatistics);
        return carModelStatistics;
    }

    @Override
    public Map<String, Map<String, Object>> getbyregiontype(String startDate, String endDate) {
        // 定义结果集：每个区域对应一个 Map，包含发生次数和成本总和
        Map<String, Map<String, Object>> regionStatistics = new HashMap<>();

        // 获取当前月份所有问题的区域数据
        List<IssueTableEntity> issueList = getIssuesByRegion(startDate, endDate);

        // 遍历问题数据，统计每个区域的发生次数和生成成本
        for (IssueTableEntity issue : issueList) {
            String regions = issue.getRegion(); // 获取区域字段（可能是多个区域，例如 "East,West,North"）
            double generationCost = issue.getQualitycost(); // 获取生成成本字段

            // 拆分区域数据，处理每个区域
            String[] regionArray = regions.split(",");
            for (String region : regionArray) {
                region = region.trim(); // 去除多余的空格

                // 如果当前区域不存在，则初始化
                regionStatistics.putIfAbsent(region, new HashMap<>());
                Map<String, Object> regionStats = regionStatistics.get(region);

                // 初始化字段 "count" 和 "totalCost"，默认为 0
                regionStats.putIfAbsent("count", 0);       // 初始化次数为 0
                regionStats.putIfAbsent("totalCost", 0.0); // 初始化成本总和为 0.0

                // 更新区域的发生次数和生成成本总和
                regionStats.put("count", (int) regionStats.get("count") + 1); // 次数加 1
                regionStats.put("totalCost", (double) regionStats.get("totalCost") + generationCost); // 成本累加
            }
        }

        System.out.println("区域统计数据：" + regionStatistics);
        return regionStatistics;
    }


    @Override
    public Map<String, Integer> gettruecurrentall() {

        Map<String, Integer> statistics = new HashMap<>();

        // 定义可能的验证结论状态
        List<String> verificationConclusions = Arrays.asList("问题添加", "问题整改", "问题验证");

        // 获取当前月份起止日期
//        String currentMonthStart = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
//        LocalDate nextMonthFirstDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
//        String nextMonthFirstDayString = nextMonthFirstDay.format(DateTimeFormatter.ISO_DATE);
//        String currentMonthEnd = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // 统计“创建”的数量（即 verification_conclusion 是 NULL 或空字符串的记录）
        statistics.put("问题添加", countIssuesBylevel("等待整改记录填写"));
        int count1 = countIssuesBylevel("等待任务下发(处理)");
        int count2 = countIssuesBylevel("等待验证指定");
        statistics.put("问题整改", count1+count2);
        statistics.put("问题验证", countIssuesBylevel("等待验证"));
        int count3 = countIssuesBylevel("结项");
        int count4 = countIssuesBylevel("未通过验证");
        statistics.put("验证已通过", count3);
        statistics.put("验证未通过", count4);

        return statistics;
    }

    @Override
    public boolean updateParentQuestion(String issueNumber) {

        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        if (issueNumber != null && !issueNumber.isEmpty()) {
            queryWrapper.eq("issue_number", issueNumber)
                    .or().eq("parent_question", issueNumber);
        }

        // 构建更新的内容
        IssueTableEntity updateEntity = new IssueTableEntity();
        updateEntity.setVerificationConclusion("结项");
        updateEntity.setLevel("结项");

        // 执行更新操作
        int updatedRows = issueTableDao.update(updateEntity, queryWrapper);

        if (updatedRows > 0) {
            System.out.println("更新成功，更新的行数：" + updatedRows);
            return true;
        } else {
            System.out.println("未找到符合条件的记录，更新失败");
            return false;
        }


//        // 获取符合条件的记录
//        List<IssueTableEntity> entities = issueTableDao.findByIssueNumber(issueNumber);
//
//        // 如果有记录需要更新
//        if (!entities.isEmpty()) {
//            for (IssueTableEntity entity : entities) {
//                entity.setVerificationConclusion("结项");
//                entity.setLevel("通过验证");
//            }
//
//            // 批量更新
//            int rowsAffected = issueTableDao.updateBatch(entities);
//            return rowsAffected > 0;
//        }

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
        String rolename = String.valueOf(role.getUserId());
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
        statistics.put("关闭", countIssuesByState());

        return statistics;
    }

    @Override
    public Map<String, Integer> getCurrentMonthStatistics() {
        Map<String, Integer> statistics = new HashMap<>();

        // 定义可能的验证结论状态
        List<String> verificationConclusions = Arrays.asList("暂缓", "结项");

        // 获取当前月份起止日期
        String currentMonthStart = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE);
        LocalDate nextMonthFirstDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        String nextMonthFirstDayString = nextMonthFirstDay.format(DateTimeFormatter.ISO_DATE);
//        String currentMonthEnd = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        // 统计“创建”的数量（即 verification_conclusion 是 NULL 或空字符串的记录）
        statistics.put("持续", countIssuesByCreation(currentMonthStart, nextMonthFirstDayString));

        // 对其他验证结论进行统计
        for (String conclusion : verificationConclusions) {
            statistics.put(conclusion, countIssuesByVerification(conclusion, currentMonthStart, nextMonthFirstDayString));
        }
        statistics.put("关闭", countIssuesBycruState(currentMonthStart, nextMonthFirstDayString));

        return statistics;
    }

    @Override
    public Map<String, Integer> getcurrentMonthInProgressCategoryStats(String startDate, String endDate) {
        Map<String, Integer> categoryStatistics = new HashMap<>();

        // 获取当前月份所有问题的 issue_category_id
        List<String> issueCategoryIds = getIssueCategoryIds(startDate, endDate);

        // 使用 issueCategoryIds 统计每个问题类型的数量
        for (String categoryId : issueCategoryIds) {
            Integer count = categoryStatistics.getOrDefault(categoryId, 0); // 获取当前类别的数量，默认值为 0
            count++; // 每次遇到相同类型的 ID，就计数加 1
            categoryStatistics.put(categoryId, count); // 更新数量
        }

        return categoryStatistics;
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
    public boolean checkReplicateIssue(Integer issueId,  String systematicClassification, String firstFaultyParts, String secondFaultyParts, String faultType, String faultModel) {
                // 构建查询条
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        // 根据传入的参数动态构建查询条件，避免查询条件为空时误查询
        queryWrapper.eq(systematicClassification != null, "Systematic_classification", systematicClassification)
                .eq(firstFaultyParts != null, "First_Faulty_parts", firstFaultyParts)
                .eq(secondFaultyParts != null, "Second_Faulty_parts", secondFaultyParts)
                .eq(faultType != null, "fault_type", faultType)
                .eq(faultModel != null, "fault_model", faultModel)
                // 排除state为"关闭"的记录
                .ne("state", "关闭");  // 添加该条件，跳过state为"关闭"的记录;


        // 查询符合条件的记录
        List<IssueTableEntity> issues = this.list(queryWrapper);

        // 判断是否存在重复记录
        boolean isDuplicate = !issues.isEmpty();

        if (isDuplicate) {
            // 使用批量更新避免循环调用 updateById
            issues.forEach(issue -> issue.setOverDue("true"));
            this.updateBatchById(issues); // 假设你有批量更新的方法
        }

        return isDuplicate; // 返回是否存在重复记录
    }





    // 获取当前月份内所有问题的 issue_category_id
    private List<String> getIssueCategoryIds(String currentMonthStart, String nextMonthFirstDayString) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", currentMonthStart)
                .lt("creation_time", nextMonthFirstDayString)
                .isNull("parent_Question").or().eq("parent_Question", "");

        // 查询当月所有问题，获取 issue_category_id 字段
        List<IssueTableEntity> issueList = this.list(queryWrapper);

        // 提取 issue_category_id 并返回
        return issueList.stream()
                .map(IssueTableEntity::getIssueCategoryId) // 获取每个记录的 issue_category_id
                .collect(Collectors.toList());
    }

    private List<String> getVehicleTypeIds(String currentMonthStart, String nextMonthFirstDayString) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", currentMonthStart)
                .lt("creation_time", nextMonthFirstDayString);

        // 查询当月所有问题，获取 vehicle_type_id 字段（车型数据）
        List<IssueTableEntity> issueList = this.list(queryWrapper);

        // 提取 vehicle_type_id 并返回
        return issueList.stream()
                .map(IssueTableEntity::getVehicleTypeId) // 获取每个记录的 vehicle_type_id
                .collect(Collectors.toList());
    }
    // 辅助方法：获取当前月份所有赔偿件数据
    private List<IssueTableEntity> getIndemnificationIssues(String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", startDate) // 筛选创建时间 >= 本月起始日期
                .lt("creation_time", endDate)  // 筛选创建时间 < 下月第一天
                .isNotNull("indemnification"); // 仅筛选有赔偿件类型的记录

        // 查询满足条件的所有记录
        return this.list(queryWrapper);
    }

    private List<IssueTableEntity> getvenderIssues(String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", startDate) // 筛选创建时间 >= 本月起始日期
                .lt("creation_time", endDate)  // 筛选创建时间 < 下月第一天
                .isNotNull("vendor"); // 仅筛选有赔偿件类型的记录

        // 查询满足条件的所有记录
        return this.list(queryWrapper);
    }

    private List<IssueTableEntity> getIssuesByDateRange(String startDate, String endDate) {
        // 构建查询条件
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", startDate) // 起始时间
                .lt("creation_time", endDate); // 结束时间

        // 查询符合条件的记录
        return this.list(queryWrapper);
    }

    private List<IssueTableEntity> getIssuesByCarModel(String currentMonthStart, String nextMonthFirstDayString) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", currentMonthStart)  // 起始日期
                .lt("creation_time", nextMonthFirstDayString)
                .isNotNull("vehicle_type_id"); // 结束日期

        // 查询当月所有问题，获取 vehicle_type_id 和 qualitycost 字段（车型数据和生成成本）
        return this.list(queryWrapper);
    }

    private List<IssueTableEntity> getIssuesByRegion(String currentMonthStart, String nextMonthFirstDayString) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", currentMonthStart)  // 起始日期
                .lt("creation_time", nextMonthFirstDayString)
                .isNotNull("region");; // 结束日期

        // 查询当月所有问题，获取 region 和 qualitycost 字段（区域数据和生成成本）
        return this.list(queryWrapper);
    }
    private Integer countAllIssues() {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        // 查询所有记录
        return this.count(queryWrapper);
    }




    // 统计“创建”的条件
    private Integer countIssuesByCreation(String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                        wrapper.isNull("verification_conclusion")
                                .or().eq("verification_conclusion", "")
                                .or().like("verification_conclusion", "持续")
                )
                .ge("creation_time", startDate)
                .le("creation_time", endDate)
                // 筛选出parent_Question字段为空或者null的数据
                .and(wrapper -> wrapper.isNull("parent_Question").or().eq("parent_Question", ""))
               ;


        return this.count(queryWrapper);
    }
    private Integer countIssuesByCreationCondition() {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                wrapper.isNull("verification_conclusion")
                        .isNull("parent_Question").or().eq("parent_Question", "")
                        .or().eq("verification_conclusion", "")
                        .or().like("verification_conclusion", "持续")
        );

        return this.count(queryWrapper);
    }

    // 统计所有问题状态
    private Integer countIssuesBylevel(String Issuestate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("level", Issuestate);


        return this.count(queryWrapper);
    }


    // 根据 verification_conclusion 统计数量，处理包含多个状态的组合（当月）
    private Integer countIssuesByVerification(String conclusion, String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();

        // 使用 FIND_IN_SET() 函数判断字段中是否包含该状态
        queryWrapper.ge("creation_time", startDate)
                .le("creation_time", endDate)
                .like("verification_conclusion", conclusion); // 使用 LIKE 查询

        return this.count(queryWrapper);
    }

    private Integer countIssuesBycruState(String startDate, String endDate) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("creation_time", startDate)
                    .le("creation_time", endDate)
                    .eq("state", "关闭"); // 使用 LIKE 查询

        return this.count(queryWrapper);
    }
    private Integer countIssuesByVerificationConclusion(String conclusion) {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("verification_conclusion", conclusion); // 使用 LIKE 查询

        return this.count(queryWrapper);
    }

    private Integer countIssuesByState() {
        QueryWrapper<IssueTableEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", "关闭"); // 使用 LIKE 查询

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
