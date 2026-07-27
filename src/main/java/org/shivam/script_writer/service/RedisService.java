package org.shivam.script_writer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public <T> T get(String key,Class<T> entityClass){
        Object value =  redisTemplate.opsForValue().get(key);

        if (value == null){
            return null;
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(value.toString(),entityClass);
        }
        catch(Exception e){
            log.error("Exception ",e);
            return null;
        }
    }
    public Boolean set(String key, Object object, Long ttl){

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key,value,ttl, TimeUnit.SECONDS);
            return true;
        }
        catch(Exception e){
            log.error("Exception ",e);
            return false;
        }
    }

    public void delete(String key){
        try{
            redisTemplate.delete(key);
        }
        catch(Exception e){
            log.error("Exception ",e);
        }
    }
}
