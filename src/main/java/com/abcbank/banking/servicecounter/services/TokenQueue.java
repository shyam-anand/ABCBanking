package com.abcbank.banking.servicecounter.services;

import com.abcbank.banking.common.redis.RedisQueue;
import com.abcbank.banking.token.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@Component
public class TokenQueue extends RedisQueue<Token, Long> {

    @Autowired
    public TokenQueue(RedisTemplate<Long, Token> redisTemplate) {
        super(redisTemplate);
    }
}
