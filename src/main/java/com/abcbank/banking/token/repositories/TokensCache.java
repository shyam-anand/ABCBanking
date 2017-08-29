package com.abcbank.banking.token.repositories;

import com.abcbank.banking.common.redis.RedisHashOpsRepository;
import com.abcbank.banking.token.models.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@Repository
public class TokensCache extends RedisHashOpsRepository<Token, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TokensCache.class);

    @Autowired
    public TokensCache(RedisTemplate<Long, Token> redisTemplate) {
        super("TOKENS", redisTemplate);
    }

    @PostConstruct
    private void init() {
        initOps();
    }

    @Override
    public void save(Token token) {
        logger.debug("Saving token {} to cache", token.getId());
        save(token.getId(), token);
    }
}
