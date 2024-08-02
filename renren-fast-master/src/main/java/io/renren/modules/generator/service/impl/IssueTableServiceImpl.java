package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.generator.dao.IssueTableDao;
import io.renren.modules.generator.entity.IssueTableEntity;
import io.renren.modules.generator.service.IssueTableService;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service("issueTableService")
public class IssueTableServiceImpl extends ServiceImpl<IssueTableDao, IssueTableEntity> implements IssueTableService {

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
        System.out.println("///"+list());
        return this.list();
    }

    @Override
    public String saveUploadedFile(MultipartFile file) throws IOException {

        // 定义文件存储的目录
        String uploadDir = "/path/to/your/upload/directory";
        // 创建目录（如果不存在）
        Files.createDirectories(Paths.get(uploadDir));

        // 生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // 保存文件到指定路径
        Files.copy(file.getInputStream(), filePath);

        // 返回文件的相对路径或URL
        return "/uploads/" + fileName;
    }

    @Override
    public void updateIssuePhoto(Integer issueId, String imagePath) {
        IssueTableEntity issueTable = this.getById(issueId);
        if (issueTable != null) {
            issueTable.setIssuePhoto(imagePath);
            this.updateById(issueTable);
        }
    }

    @Override
    public String getuserinfo() {
        SysUserEntity role = ShiroUtils.getUserEntity();
        String rolename = role.getUsername();
        return rolename;
    }

}