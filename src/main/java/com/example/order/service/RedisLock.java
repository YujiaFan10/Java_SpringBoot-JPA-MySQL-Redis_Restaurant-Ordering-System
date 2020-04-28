package com.example.order.service;

import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.core.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * redis: add lock
     * @param key
     * @param value: current time + lock time
     * @return
     */
    public boolean lock(String key, String value){
        if(redisTemplate.opsForValue().setIfAbsent(key, value)){
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        //if the lock expires
        if(!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
            String previousValue = redisTemplate.opsForValue().getAndSet(key, value);
            if(!StringUtils.isEmpty(previousValue) && previousValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    /**
     * redis: unlock
     * @param key
     * @param value
     */
    public void unlock(String key, String value){
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            // if currentValue == the value put in
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                // redis: delete key
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e){
            log.error("[redis lock]: unlock error, {}", e);
        }
    }

}
