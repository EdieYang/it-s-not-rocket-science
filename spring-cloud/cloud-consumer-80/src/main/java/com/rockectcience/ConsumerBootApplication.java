package com.rockectcience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 消费者启动类
 *
 * @author Eddie
 * @since
 */
@SpringBootApplication
@EnableEurekaClient
public class ConsumerBootApplication {


    public static void main(String[] args) {
        SpringApplication.run(ConsumerBootApplication.class, args);
    }
}
