package com.abcbank.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
@SpringBootApplication
public class ABCBanking {

    public static void main(String[] args) {
        SpringApplication.run(ABCBanking.class, args);
    }
}
