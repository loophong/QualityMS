package io.renren.modules.taskmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.taskmanagement.dao.FileDao;
import io.renren.modules.taskmanagement.entity.FileEntity;
import io.renren.modules.taskmanagement.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {
}
