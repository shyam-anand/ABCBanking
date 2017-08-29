package com.abcbank.banking.common.redis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
public class RedisQueue<T, ID> {

    private ListOperations<ID, T> listOps;

    public RedisQueue(RedisTemplate<ID, T> redisTemplate) {
        listOps = redisTemplate.opsForList();
    }

    public Collection<T> findAll(ID id) {
        return listOps.range(id, 0L, listOps.size(id) - 1);
    }

    public void push(ID id, T t) {
        listOps.leftPush(id, t);
    }

    public T pop(ID id) {
        return listOps.rightPop(id);
    }
}
