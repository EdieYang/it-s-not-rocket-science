package com.rocketscience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Pay服务启动类
 *
 * @author Eddie
 * @since
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class PayBootApplication8002 {

    public static void main(String[] args) {
        SpringApplication.run(PayBootApplication8002.class, args);
    }
}
