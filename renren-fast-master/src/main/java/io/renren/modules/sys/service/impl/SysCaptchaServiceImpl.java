/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.modules.sys.dao.SysCaptchaDao;
import io.renren.modules.sys.entity.SysCaptchaEntity;
import io.renren.modules.sys.service.SysCaptchaService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> implements SysCaptchaService {
    @Autowired
    private Producer producer;

//    @Override
//    public BufferedImage getCaptcha(String uuid) {
//        if(StringUtils.isBlank(uuid)){
//            throw new RRException("uuid不能为空");
//        }
//        //生成文字验证码
//        String code = producer.createText();
//
//        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
//        captchaEntity.setUuid(uuid);
//        captchaEntity.setCode(code);
//        //5分钟后过期
//        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
//        this.save(captchaEntity);
//
//        return producer.createImage(code);
//    }

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new RRException("uuid不能为空");
        }

        Random random = new Random(); // 创建Random对象
        int num1 = random.nextInt(10) + 1; // 生成1到10之间的随机数
        int num2 = random.nextInt(10) + 1; // 生成1到10之间的随机数
        String operator;  // 运算符
        int result = 0;   // 结果

        // 随机选择一个运算符
        switch (random.nextInt(4)) { // 生成0到3之间的随机数
            case 0:
                operator = "+";
                result = num1 + num2; // 加法
                break;
            case 1:
                operator = "-";
                result = num1 - num2; // 减法
                break;
            case 2:
                operator = "*";
                result = num1 * num2; // 乘法
                break;
            case 3:
                operator = "/";
                // 防止除数为0
                if (num2 == 0) {
                    num2 = 1; // 确保除法有效
                }
                result = num1 / num2; // 除法
                break;
            default:
                operator = "+";
                result = num1 + num2; // 默认加法
                break;
        }

        // 创建验证码文本
        String code = num1 + " " + operator + " " + num2 + " = ?";

        SysCaptchaEntity captchaEntity = new SysCaptchaEntity();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(String.valueOf(result)); // 将结果存储为验证码的实际值
        // 5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        this.save(captchaEntity);

        // 生成图像并返回
        return producer.createImage(code); // 这里图像内容为算式问题
    }


    @Override
    public boolean validate(String uuid, String code) {
        SysCaptchaEntity captchaEntity = this.getOne(new QueryWrapper<SysCaptchaEntity>().eq("uuid", uuid));
        if(captchaEntity == null){
            return false;
        }

        //删除验证码
        this.removeById(uuid);

        if(captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()){
            return true;
        }

        return false;
    }
}
