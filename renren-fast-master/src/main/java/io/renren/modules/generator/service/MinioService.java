package io.renren.modules.generator.service;


import io.minio.MinioClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MinioService {

    @Resource
    private MinioClient minioClient;


    public void testMinioClent(){
        System.out.println(minioClient);
    }

}
