/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.config;

import io.renren.modules.sys.jwt.JWTFilter;
import io.renren.modules.sys.jwt.JWTRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public SecurityManager securityManager(JWTRealm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
//        shiroFilter.setSecurityManager(securityManager);
//
//        //jwt token过滤
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("jwt", new JWTFilter());
//        shiroFilter.setFilters(filters);
//
//        Map<String, String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/webjars/**", "anon");
//        filterMap.put("/druid/**", "anon");
//        filterMap.put("/app/**", "anon");
//        filterMap.put("/sys/login", "anon");
//        filterMap.put("/swagger/**", "anon");
//        filterMap.put("/v2/api-docs", "anon");
//        filterMap.put("/swagger-ui.html", "anon");
//        filterMap.put("/swagger-resources/**", "anon");
//        filterMap.put("/captcha.jpg", "anon");
//        filterMap.put("/aaa.txt", "anon");
////        filterMap.put("/**", "anon");
//        filterMap.put("/**", "jwt");
//        shiroFilter.setFilterChainDefinitionMap(filterMap);
//
//        return shiroFilter;
//    }

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        // JWT token过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JWTFilter());
        shiroFilter.setFilters(filters);

        // 过滤规则配置
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 放行文件上传的路径
        filterMap.put("/renren-fast/**", "anon");
        // 放行静态资源
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/aaa.txt", "anon");



        // 所有其他请求都需要JWT认证
        filterMap.put("/**", "jwt");

        // 设置过滤器链
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        // 打印过滤器链的配置
        logger.info("Shiro Filter Config: {}", filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
