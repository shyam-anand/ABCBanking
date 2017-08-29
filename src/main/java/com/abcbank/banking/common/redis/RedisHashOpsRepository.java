package com.abcbank.banking.common.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@SuppressWarnings("unchecked")
public abstract class RedisHashOpsRepository<T, ID> {

    protected final String KEY;
    protected final RedisTemplate<ID, T> redisTemplate;
    protected HashOperations hashOps;

    public RedisHashOpsRepository(String key, RedisTemplate<ID, T> redisTemplate) {
        KEY = key;
        this.redisTemplate = redisTemplate;
    }

    protected void initOps() {
        hashOps = redisTemplate.opsForHash();
    }

    protected void save(ID id, T t) {
        hashOps.put(KEY, id, t);
    }

    abstract public void save(T t);

    public T find(ID id) {
        return (T) hashOps.get(KEY, id);
    }

    public Map<Object, Object> findAll() {
        return hashOps.entries(KEY);
    }

    public void delete(ID id) {
        hashOps.delete(KEY, id);
    }
}
