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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // 解析方法
    private List<String> parseRelatedIssueIds(String relatedIssueIds) {
        if (relatedIssueIds != null && !relatedIssueIds.trim().isEmpty()) {
            return Arrays.stream(relatedIssueIds.split(","))
                    .map(String::trim) // 去除每个编号的空格
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // 如果没有记录，则返回空列表
    }




}
