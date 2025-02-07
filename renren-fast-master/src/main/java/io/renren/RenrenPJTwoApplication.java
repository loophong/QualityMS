/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class RenrenPJTwoApplication {



	public static void main(String[] args) {

//		System.setProperty("user.dir", "E:/QualityMS/renren-fast-master/src/main/resources");
		SpringApplication.run(RenrenPJTwoApplication.class, args);
	}

}