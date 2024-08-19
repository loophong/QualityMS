package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("issueTableService")
public class IssueTableServiceImpl extends ServiceImpl<IssueTableDao, IssueTableEntity> implements IssueTableService {
    private final String uploadDir;

    @Value("${file.upload.dir}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.address}")
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
        if (uploadPath == null) {
            throw new IllegalStateException("Upload path is not configured.");
        }

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
}
