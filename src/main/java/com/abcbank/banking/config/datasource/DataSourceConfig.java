package com.abcbank.banking.config.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
@Configuration
public class DataSourceConfig {

    private final String dataSourceUrl;
    private final String dataSourceUser;
    private final String dataSourcePassword;
    private final String dataSourceDriver;

    @Autowired
    public DataSourceConfig(@Value("${spring.datasource.url}") String url,
                            @Value("${spring.datasource.username}") String user,
                            @Value("${spring.datasource.password}") String password,
                            @Value("${spring.datasource.driver-class-name}") String driver) {
        dataSourceUrl = url;
        dataSourceUser = user;
        dataSourcePassword = password;
        dataSourceDriver = driver;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceDriver);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUser);
        dataSource.setPassword(dataSourcePassword);

        return dataSource;
    }
}
