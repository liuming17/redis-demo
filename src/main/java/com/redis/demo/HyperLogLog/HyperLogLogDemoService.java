package com.redis.demo.HyperLogLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by liuming on 2018/1/30.
 */
@Service
public class HyperLogLogDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private RedisTemplate redisTemplate ;

    /**
     * 	PFADD key element [element ...] 添加指定元素到 HyperLogLog 中。
     * @param key
     * @param element
     */
    public void pfadd(String key, Object element){
        redisTemplate.opsForHyperLogLog().add(key , element) ;
    }

    /**
     * PFCOUNT key [key ...] 返回给定 HyperLogLog 的基数估算值。
     * @param key
     * @return
     */
    public Long pfcount (String... key){
        return redisTemplate.opsForHyperLogLog().size(key) ;
    }

    /**
     * PFMERGE destkey sourcekey [sourcekey ...] 将多个 HyperLogLog 合并为一个 HyperLogLog
     * @param key1
     * @param key2
     * @param newKey
     */
    public void pfmerge(String newKey, String key1, String key2){
        redisTemplate.opsForHyperLogLog().union(newKey, key1, key2) ;
    }

}
