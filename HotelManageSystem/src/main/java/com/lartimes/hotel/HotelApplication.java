package com.lartimes.hotel;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/17 18:19
 */
@ConfigurationPropertiesScan(basePackages = {"com.lartimes.hotel.conf"})
@SpringBootApplication
@Import(RocketMQAutoConfiguration.class)
@MapperScan("com.lartimes.hotel.mapper")
@EnableScheduling
public class HotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class ,args);
    }
}
