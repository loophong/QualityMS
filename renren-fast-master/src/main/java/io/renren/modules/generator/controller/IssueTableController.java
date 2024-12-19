package io.renren.modules.generator.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.entity.IssueUtils;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.generator.service.MinioService;
import org.apache.http.HttpStatus;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 问题表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2024-07-22 11:15:58
 */
@RestController
@RequestMapping("generator/issuetable")
public class IssueTableController {
    @Autowired
    private IssueTableService issueTableService;

    @Resource
    MinioClient minioClient;


    SimpleDateFormat saf = new SimpleDateFormat("/yyyy/MM/dd");

    private static final String uploadDir = System.getProperty("user.dir") + "/file/"; // 请确保这个路径已存在或可写

    /**
     * 问题知识库
     */
    @RequestMapping("/verlist")
    @RequiresPermissions("generator:issuetable:list")
    public R finishedList(@RequestParam Map<String, Object> params){
        PageUtils page =  issueTableService.queryPageFinishedList(params);
        return R.ok().put("page", page);
    }
    /**
     * 上传图片
     */
//    @Value("${file.upload-dir}")
    @PostMapping("/upload")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadImage(@RequestParam("file") MultipartFile file ) {
        System.out.println("开始上传" + uploadDir);
        //原始文化名
        String originName = file.getOriginalFilename();
        //时间戳
        String flag = System.currentTimeMillis() + "" ;
        try{
            if (!FileUtil.isDirectory(uploadDir));{
                FileUtil.mkdir(uploadDir);
            }
            FileUtil.writeBytes(file.getBytes(),uploadDir + flag + "-" + originName);
            System.out.println("上传成功");
        }catch (Exception e){
            System.out.println("上传失败");
        }
        System.out.println("上传结束" + flag);
        return R.ok().put("uploadurl", flag);
    }

    /**
     * 文件获取
     */
    @RequestMapping("/{flag}")
    @RequiresPermissions("generator:issuetable:update")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) throws Exception {
        if (!FileUtil.isDirectory(uploadDir));{
            FileUtil.mkdir(uploadDir);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(uploadDir);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try{
            if (StrUtil.isNotEmpty(avatar)){
//                response.addHeader( "Content-Disposition", "attachment;filename="+ URLEncoder.encode(avatar,"UTF-8"));
//                response.setContentType("application/octet-stream");
                // 获取文件的扩展名，判断文件类型
                String fileType = FileUtil.extName(avatar).toLowerCase();

                // 根据文件类型设置合适的 Content-Type
                switch (fileType) {
                    case "jpg":
                    case "jpeg":
                        response.setContentType("image/jpeg");
                        break;
                    case "png":
                        response.setContentType("image/png");
                        break;
                    case "gif":
                        response.setContentType("image/gif");
                        break;
                    case "pdf":
                        response.setContentType("application/pdf");
                        break;
                    default:
                        response.setContentType("application/octet-stream");
                        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                        break;
                }
                byte[] bytes = FileUtil.readBytes(uploadDir + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                return ;
            }

        } catch (Exception e ){
            System.out.println("文件下载失败");
        };
//        return R.ok();
    }


    /**
     * 检查车号和问题类别是否重复
     */
//    @RequestMapping("/checkDuplicateIssue")
//    @RequiresPermissions("generator:issuetable:list")
//    public R checkDuplicateIssue(@RequestBody Map<String, List<String>> params) {
//        List<String> vehicleNumbers = params.get("vehicleNumbers");
//        List<String> issueCategoryIds = params.get("issueCategoryIds");
//
//        System.out.println("开始检查车号和问题类别是否重复1");
//        if (vehicleNumbers == null || issueCategoryIds == null) {
//            return R.error("缺少参数: vehicleNumbers 或 issueCategoryIds");
//        }
//
//        // 调用服务层方法检查是否有重复的车号和问题类别
//        boolean isDuplicate = issueTableService.checkDuplicateIssue(vehicleNumbers, issueCategoryIds);
//
//        if (isDuplicate) {
//            return R.error("此车已发生过相同问题");
//        }
//
//        return R.ok();
//    }
    @RequestMapping("/checkDuplicateIssue")
    @RequiresPermissions("generator:issuetable:list")
    public R checkDuplicateIssue(@RequestBody Map<String, Object> params) {

        // 获取前端传递的车号列表和问题类别ID字符串
        List<String> vehicleNumbers = (List<String>) params.get("vehicleNumbers"); // 车号是一个数组
        String issueCategoryIds = (String) params.get("issueCategoryId"); // 问题类别ID是一个字符串

//        System.out.println("开始检查车号和问题类别是否重复"+issueCategoryIds+","+vehicleNumbers);

        // 参数校验
        if (vehicleNumbers == null || issueCategoryIds == null) {
            return R.error("缺少参数: vehicleNumbers 或 issueCategoryId");
        }

        // 将问题类别ID的字符串按逗号分割成列表
//        List<String> issueCategoryIdList = Arrays.asList(issueCategoryIds.split(",")); // 将逗号分隔的字符串转为列表

        // 调用服务层方法检查是否有重复的车号和问题类别
        boolean isDuplicate = issueTableService.checkDuplicateIssue(vehicleNumbers, issueCategoryIds);
//        System.out.println("结束检查车号和问题类别是否重复"+isDuplicate);
        if (isDuplicate) {
            return R.error("此车已发生过相同问题");
        }

        return R.ok();
    }

    @RequestMapping("/checkReplicateIssue")
    @RequiresPermissions("generator:issuetable:list")
    public R checkReplicateIssue(@RequestBody Map<String, Object> params) {
        // 获取前端传递的车号列表和问题类别ID字符串
        Integer issueId = (Integer) params.get("issueId"); // 问题ID是一个整数
        String issueCategoryIds = (String) params.get("issueCategoryId"); // 问题类别ID是一个字符串
        String systematicClassification = (String) params.get("systematicClassification"); // 系统分类是一个字符串
        String firstFaultyParts = (String) params.get("firstFaultyParts"); // 问题类别ID是一个字符串
        String secondFaultyParts = (String) params.get("secondFaultyParts"); // 问题类别ID是一个字符串
        String faultType = (String) params.get("faultType"); // 问题类别ID是一个字符串
        String faultModel = (String) params.get("faultModel"); // 问题类别ID是一个字符串


        // 调用服务层方法检查是否有重复的车号和问题类别
        boolean isDuplicate = issueTableService.checkReplicateIssue(issueId,systematicClassification,firstFaultyParts,secondFaultyParts,faultType,faultModel);
        System.out.println("结束检查车号和问题类别是否重复"+isDuplicate);
        if (isDuplicate) {
            return R.ok("相同问题已发生");
        }

        return R.ok();
    }




    /**
     * Excel上传
     */
    @PostMapping("/uploadExcel")
    @RequiresPermissions("generator:issuetable:update")
    public R uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("接收到的文件名: " + file.getOriginalFilename());
            return issueTableService.uploadExcelFile(file);
        } catch (IOException e) {
            return R.error("文件上传时发生错误: " + e.getMessage());
        }
    }

    /**
     * Excel模板下载
     */
    @GetMapping("/generateTemplate")
    @RequiresPermissions("generator:issuetable:list")
    public void generateTemplate(HttpServletResponse response) {
        try {
            Workbook workbook = issueTableService.generateTemplate(); // 调用服务层生成模板

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=IssueTableTemplate.xlsx");

            // 写入Excel到响应
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 关联相关任务
     */
    @RequestMapping("/connection")
    @RequiresPermissions("generator:issuetable:list")
    public R connectionIssue(@RequestBody Map<String, Long> params) {
        Long issueId = params.get("issueId");
        if (issueId == null) {
            return R.error("缺少参数: issueId");
        }
        System.out.println("获取的issueId ："+issueId);
        issueTableService.connectionIssue(issueId);
        return R.ok();

    }

    /**
     * 修改原问题状态
     */
    @PostMapping("/updateParentQuestion")
    @RequiresPermissions("generator:issuetable:update")
    public R updateParentQuestion(@RequestBody Map<String, Object> params) {
        String issueNumber = (String) params.get("issuenumber");
        if (issueNumber == null || issueNumber.isEmpty()) {
            return R.error("问题编号不能为空");
        }
        boolean success = issueTableService.updateParentQuestion(issueNumber);
        if (success) {
            return R.ok().put("msg", "原问题状态更新成功");
        } else {
            return R.error("更新失败，请检查问题编号是否正确");
        }
    }

    /**
     * 关闭相关任务
     */
    @RequestMapping("/closeRelatedTasks/{issueId}/{closeRelated}")
    @RequiresPermissions("generator:issuetable:delete")
    public R closeRelatedTasks(
            @PathVariable("issueId") Long issueId,
            @PathVariable("closeRelated") Integer closeRelated) {
        // 调用服务层方法，并传递新参数
        return issueTableService.closeRelatedTasks(issueId, closeRelated);
    }

    /**
     * 问题编号
     */
    @RequestMapping("/newIssueNumber")
    @RequiresPermissions("generator:issuetable:save")
    public R newIssueNumber() {
        String useID = issueTableService.newIssueNumber(); // 直接调用服务层方法
        System.out.println("获取的新编号为：" + useID);
        return R.ok().put("useID", useID);
    }

    /**
     * 问题编号(未通过验证)
     */
    @RequestMapping("/connectNumber")
    @RequiresPermissions("generator:issuetable:save")
    public R connectNumber(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        String useID = issueTableService.connectNumber(id); // 直接调用服务层方法
        System.out.println("获取的新编号为：" + useID);
        return R.ok().put("usedID", useID);
    }

    /**
     * 获取问题统计（总数）
     */
    @RequestMapping("/currentMonth")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R getCurrentMonthVerificationConclusionStatistics() {
        Map<String, Integer> stats = issueTableService.getCurrentMonthVerificationConclusionStatistics();
        // 打印返回给前端的数据
        System.out.println("返回前端的统计数据: " + stats);

        return R.ok().put("stats", stats);
    }
    /**
     * 获取问题统计（当月统计不同状态）
     */
    @RequestMapping("/truecurrentMonth")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R getCurrentMonthStatistics() {
        Map<String, Integer> stats = issueTableService.getCurrentMonthStatistics();
        // 打印返回给前端的数据
//        System.out.println("返回前端的统计数据: " + stats);

        return R.ok().put("stats", stats);
    }

    /**
     * 获取问题统计（当月统计不同状态）
     */
    @RequestMapping("/truecurrentall")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R gettruecurrentall() {
        Map<String, Integer> stats = issueTableService.gettruecurrentall();
        // 打印返回给前端的数据
//        System.out.println("返回前端的统计数据: " + stats);

        return R.ok().put("stats", stats);
    }

    /**
     * 获取问题统计（当月统计不同类型）
     */
    @RequestMapping("/currentMonthInProgressCategoryStats")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R getcurrentMonthInProgressCategoryStats() {
        Map<String, Integer> stats = issueTableService.getcurrentMonthInProgressCategoryStats();
        // 打印返回给前端的数据
//        System.out.println("返回前端的统计数据: " + stats);

        return R.ok().put("stats", stats);
    }

    /**
     * 获取按月份统计的总数（按年份）
     * @param year 年份
     * @return 返回统计数据
     */
    @RequestMapping("/monthlyStats")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R getMonthlyCount(@RequestParam("year") int year) {
        // 获取按月统计的结果
        Map<String, Integer> stats = issueTableService.getMonthlyCountByYear(year);

        // 打印返回给前端的数据
//        System.out.println("返回前端的统计数据: " + stats);

        // 返回响应
        return R.ok().put("stats", stats);
    }

    /**
     * 获取按月份统计的重复问题总数（按年份）
     * @param year 年份
     * @return 返回统计数据
     */
    @RequestMapping("/monthlyDuplicateStats")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R getmonthlyDuplicateStats(@RequestParam("year") int year) {
        // 获取按月统计的结果
        Map<String, Integer> stats = issueTableService.getmonthlyDuplicateStats(year);

        // 计算重复问题的总数
        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        // 打印返回给前端的数据
        System.out.println("返回前端的统计数据: " + total);

        // 返回响应
        return R.ok().put("stats", stats).put("total", total);
    }


    /**
     * 问题完成率
     */
//    @Value("${file.upload-dir}")
    // 获取问题完成率
    @RequestMapping("/completionRate")
    @RequiresPermissions("generator:issuetable:list")
    public R getCompletionRate() {
        Map<String, Integer> completionRate = issueTableService.getCurrentMonthCompletionRate();
        System.out.println("返回前端的完成率数据: " + completionRate);
        return R.ok().put("completionRate", completionRate);
    }

    /**
     * 获取问题总数
     */
    @RequestMapping("/totalIssue")
    @RequiresPermissions("generator:issuetable:list") // 权限控制
    public R gettotalIssue() {
        List<IssueTableEntity> issues = issueTableService.listAll();
        int totalIssue = issues.size();
        return R.ok().put("totalIssue" ,totalIssue);
    }

    /**
     * 获取所有问题列表
     */
    @RequestMapping("/issuesid")
    @RequiresPermissions("generator:issuetable:list")
    public R list(){
//        System.out.println("=====获取问题列表----开始");
        List<IssueTableEntity> issues = issueTableService.listAll();
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 0);
//        response.put("issueTable", issues);
//        System.out.println(response);
//        System.out.println("=====获取问题列表----结束");
        return R.ok().put("issues", issues);
    }

    /**
     * 获取所有问题列表
     */
    @RequestMapping("/issuesAll")
    @RequiresPermissions("generator:issuetable:list")
    public List<IssueTableEntity> listAll(){
//        System.out.println("=====获取问题列表----开始");
        List<IssueTableEntity> list = issueTableService.listAll();
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 0);
//        response.put("issueTable", issues);
//        System.out.println(response);
//        System.out.println("=====获取问题列表----结束");
        return list;
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/useinfo")
    @RequiresPermissions("generator:issuetable:list")
    public R listuserinfo(){
        System.out.println("=====获取用户信息----开始");
        String userinfo = issueTableService.getuserinfo();
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 0);
//        response.put("issueTable", userinfo);
        System.out.println(userinfo);
        System.out.println("=====获取用户信息----结束");
        return R.ok().put("userinfo", userinfo);
    }
    /**
     * 获取任务详细统计信息
     */
    @RequestMapping("/taskDetails/{issueNumber}")
    @RequiresPermissions("generator:issuetable:list")
    public R getTaskDetails(@PathVariable("issueNumber") String issueNumber) {
        System.out.println("开始统计问题相关问题++++++++++");
        // 调用服务层方法获取相关统计信息
        Map<String, Integer> stats = issueTableService.getTaskDetails(issueNumber);
        System.out.println("问题相关任务数据：" + stats);
        // 检查返回的数据
        if (stats != null && !stats.isEmpty()) {
            return R.ok().put("result", stats);
        } else {
            return R.error("未找到相关任务信息");
        }
    }
    /**
     * 查询列表
     */
    @RequestMapping("/Querylist")
    @RequiresPermissions("generator:issuetable:list")
    public R Querylist(@RequestParam Map<String, Object> params){
        PageUtils page = issueTableService.QueryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询并复用列表
     */
    @RequestMapping("/QueryFulist")
    @RequiresPermissions("generator:issuetable:list")
    public R QueryFulist(@RequestParam Map<String, Object> params){
        PageUtils page = issueTableService.QueryFuPage(params);
        System.out.println("-------------------------------------------"+page);
        return R.ok().put("page", page);
    }

    /**
     * 问题列表（创建人可见）
     */
    @RequestMapping("/listcreator")
    @RequiresPermissions("generator:issuetable:list")
    public R listcreator(@RequestParam Map<String, Object> params){
        PageUtils page = issueTableService.queryPagecreator(params);
//        PageUtils page = issueTableService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 问题列表完整
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuetable:list")
    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = issueTableService.queryPagecreator(params);
        PageUtils page = issueTableService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByassociate/{associatedRectificationRecords}")
    @RequiresPermissions("generator:issuetable:info")
    public R infoByassociate(@PathVariable("associatedRectificationRecords") String associatedRectificationRecords){
        IssueTableEntity issueTable = issueTableService.getByassociate(associatedRectificationRecords);
//        System.out.println("获取关联问题+" + issueTable);
        return R.ok().put("issueTable", issueTable);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByIssueNumber/{issueNumber}")
    @RequiresPermissions("generator:issuetable:info")
    public R infoBynumber(@PathVariable("issueNumber") String issueNumber){
//        System.out.println("开始获取+++++++++++++++++++++++++++++");
        IssueTableEntity issueTable = issueTableService.getByissueNumber(issueNumber);
//        System.out.println("获取问题为："+issueTable);
        return R.ok().put("issueTable", issueTable);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{issueId}")
    @RequiresPermissions("generator:issuetable:info")
    public R info(@PathVariable("issueId") Integer issueId){
        IssueTableEntity issueTable = issueTableService.getById(issueId);

        return R.ok().put("issueTable", issueTable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:issuetable:save")
    public R save(@RequestBody IssueTableEntity issueTable){
        issueTableService.save(issueTable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:issuetable:update")
    public R update(@RequestBody IssueTableEntity issueTable){

        System.out.println("开始修改整改数据"+issueTable);
        issueTableService.updateById(issueTable);
        System.out.println("结束修改整改数据");
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:issuetable:delete")
    public R delete(@RequestBody Integer[] issueIds){
        issueTableService.removeAll(issueIds);
        issueTableService.removeByIds(Arrays.asList(issueIds));

        return R.ok();
    }

}
