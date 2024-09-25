package io.renren.modules.generator.controller;

import io.minio.*;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.entity.IssueUtils;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.generator.service.MinioService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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

//    @Resource
//    MinioClient minioClient;


    SimpleDateFormat saf = new SimpleDateFormat("/yyyy/MM/dd");

    private final String uploadDir = "C:/uploads"; // 请确保这个路径已存在或可写

    /**
     * 上传图片
     */
//    @Value("${file.upload-dir}")
    @PostMapping("/upload")
    @RequiresPermissions("generator:issuetable:update")
    @CrossOrigin(origins = "http://localhost:8001") // 只允许这个源
    public R uploadImage(@RequestParam("file") MultipartFile file , HttpServletRequest request) {
        String originName = file.getOriginalFilename();
        String format = saf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/") + format;
        File floder = new File(realPath);
        if (!floder.exists()){
            floder.mkdirs();
        }
        String newName = UUID.randomUUID().toString()+".jpg";
        try {
            file.transferTo(new File(floder,newName));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"/renren-fast" + format+"/" + newName;
            System.out.println("获取的url"+url);
//            R.ok().put("data",url);
            return R.ok().put("data",url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return R.ok();
    }

//    @PostMapping("/minioimage")
//    @RequiresPermissions("generator:issuetable:update")
//    public R uploadImageMinio(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//        try {
//            // 检查存储桶是否存在
//            String bucketName = "your-bucket-name";  // 替换为你的存储桶名称
//            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//
//            if (!isBucketExists) {
//                // 如果存储桶不存在，可以选择创建一个新的存储桶
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//            }
//
//            // 生成唯一的文件名
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//            // 上传文件到 MinIO
//            minioClient.putObject(PutObjectArgs.builder()
//                    .bucket(bucketName)  // 设置存储桶名称
//                    .object(fileName)    // 设置上传的对象名称
//                    .stream(file.getInputStream(), file.getSize(), -1) // 文件流
//                    .contentType(file.getContentType()) // 文件类型
//                    .build());
//
//            // 构造文件的访问链接
//            String fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
//                            .bucket(bucketName)
//                            .object(fileName)
//                            .build()
//                    );
//            System.out.println("获得图片的路径为;" +fileUrl);
//            // 返回成功响应
//            return R.ok().put("fileUrl", fileUrl);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.error("文件上传失败: " + e.getMessage());
//        }
//    }


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
     * 关闭相关任务
     */
    // 关闭相关任务
    @RequestMapping("/closeRelatedTasks/{issueId}")
    @RequiresPermissions("generator:issuetable:delete")
    public R closeRelatedTasks(@PathVariable("issueId") Long issueId) {
        return issueTableService.closeRelatedTasks(issueId); // 直接调用服务层方法
    }

    /**
     * 获取当月问题统计
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
     * 获取所有问题列表
     */
    @RequestMapping("/issuesid")
    @RequiresPermissions("generator:issuetable:list")
    public R list(){
        System.out.println("=====获取问题列表----开始");
        List<IssueTableEntity> issues = issueTableService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", issues);
        System.out.println(response);
        System.out.println("=====获取问题列表----结束");
        return R.ok().put("issues", issues);
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/useinfo")
    @RequiresPermissions("generator:issuetable:list")
    public R listuserinfo(){
        System.out.println("=====获取用户信息----开始");
        String userinfo = issueTableService.getuserinfo();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("issueTable", userinfo);
        System.out.println(response);
        System.out.println("=====获取用户信息----结束");
        return R.ok().put("userinfo", userinfo);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:issuetable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = issueTableService.queryPage(params);
        return R.ok().put("page", page);
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
		issueTableService.removeByIds(Arrays.asList(issueIds));

        return R.ok();
    }

}
