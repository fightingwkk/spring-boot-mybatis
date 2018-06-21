package com.neo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.TimeZone;
@ServletComponentScan  //扫描servlet
@MapperScan("com.neo.mapper")//配置需要扫描的Mapper位置
@EnableScheduling
@EnableCaching//开启缓存
@EnableDiscoveryClient  //该注解能激活Eureka中的DiscoveryClient实现
@SpringBootApplication  //等同于@Configuration @EnableAutoConfiguration @ComponentScan三个注解
public class Application {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar calendar = Calendar.getInstance();
		System.out.println("目前时间"+calendar.getTime());

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
} 
