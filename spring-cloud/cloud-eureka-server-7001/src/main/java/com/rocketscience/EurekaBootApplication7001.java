package com.rocketscience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 注册中心启动类
 *
 * @author Eddie
 * @since
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaBootApplication7001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaBootApplication7001.class, args);
    }

}
