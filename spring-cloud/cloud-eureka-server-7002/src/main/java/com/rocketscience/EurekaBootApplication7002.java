package com.rocketscience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 注册中心2 启动类
 *
 * @author Eddie
 * @since
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaBootApplication7002 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaBootApplication7002.class, args);
    }
}
