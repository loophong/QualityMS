package io.renren.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    // 从配置文件中注入上传文件的实际存储路径
//    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /renren-fast/** 映射到文件系统中的实际上传路径
        registry.addResourceHandler("/renren-fast/**")
                .addResourceLocations("file:" + uploadPath);
    }
}