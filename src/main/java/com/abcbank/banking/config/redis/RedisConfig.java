package com.abcbank.banking.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
@Configuration
public class RedisConfig {

    private final String redisHost;
    private final int redisPort;
    private final String redisPassword;

    @Autowired
    public RedisConfig(@Value("${spring.redis.host}") String host,
                       @Value("${spring.redis.port}") int port,
                       @Value("${spring.redis.password}") String password) {
        redisHost = host;
        redisPort = port;
        redisPassword = password;
    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        if (null != redisPassword)
            jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setUsePool(false);
        return jedisConnectionFactory;
    }
}
