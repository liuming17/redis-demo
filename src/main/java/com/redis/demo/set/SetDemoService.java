package com.redis.demo.set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by liuming on 2018/1/29.
 */
@Service
public class SetDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private RedisTemplate redisTemplate ;

    /**
     * SADD key member1 [member2] 向集合添加一个或多个成员
     * @param key
     * @param value
     */
    public long add(String key , Object value){
        return redisTemplate.opsForSet().add(key , value) ;
    }

    /**
     * 	SCARD key 获取集合的成员数
     * @param key
     * @return
     */
    public long sCard(String key){
        return (Long)redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer() ;
                return redisConnection.sCard(serializer.serialize(key));
            }
        }) ;
    }

    /**
     * 	SDIFF key1 [key2] 返回给定所有集合的差集
     * @param key1
     * @param key2
     * @return
     */
    public Set sdiff(String key1 , String key2){
        Set set = redisTemplate.opsForSet().difference(key1 , key2) ;
        return set ;
    }

    /**
     * SDIFFSTORE destination key1 [key2] 返回给定所有集合的差集并存储在 destination 中
     * @param newKey
     * @param key1
     * @param key2
     * @return
     */
    public Long sidffAndSet(String newKey, String key1, String key2){
        return redisTemplate.opsForSet().differenceAndStore(key1, key2, newKey) ;
    }

    /**
     * SINTER key1 [key2] 返回给定所有集合的交集
     * @param key1
     * @param key2
     * @return
     */
    public Set sinter(String key1, String key2){
        return redisTemplate.opsForSet().intersect(key1 , key2) ;
    }

    /**
     * SINTERSTORE destination key1 [key2] 返回给定所有集合的交集并存储在 destination 中
     * @param key1
     * @param key2
     * @param newKey
     * @return
     */
    public Long sinterAndSet(String newKey , String key1 , String key2){
        return redisTemplate.opsForSet().intersectAndStore(key1 , key2, newKey) ;
    }

    /**
     * SISMEMBER key member 判断 member 元素是否是集合 key 的成员
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key , Object member){
        return redisTemplate.opsForSet().isMember(key , member) ;
    }

    /**
     * SMEMBERS key 返回集合中的所有成员
     * @param key
     * @return
     */
    public Set smembers(String key){
        return redisTemplate.opsForSet().members(key) ;
    }

    /**
     * 	SMOVE source destination member 将 member 元素从 source 集合移动到 destination 集合
     * @param key
     * @param value
     * @param newKey
     * @return
     */
    public Boolean smove(String key, Object value, String newKey){
        return redisTemplate.opsForSet().move(key , value, newKey) ;
    }

    /**
     * 	SPOP key 移除并返回集合中的一个随机元素
     * @param key
     * @return
     */
    public Object spop(String key){
        return redisTemplate.opsForSet().pop(key) ;
    }

    /**
     * SRANDMEMBER key [count] 返回集合中一个或多个随机数
     * @param key
     * @param count
     * @return
     */
    public List srandmember(String key , long count){
        return redisTemplate.opsForSet().randomMembers(key , count) ;
    }

    /**
     * SREM key member1 [member2] 移除集合中一个或多个成员
     * @param key
     * @param values
     * @return
     */
    public Long srem(String key , Object... values){
        return redisTemplate.opsForSet().remove(key , values) ;
    }

    /**
     * 	SUNION key1 [key2] 返回所有给定集合的并集
     * @param key1
     * @param key2
     * @return
     */
    public Set sunion(String key1 , String key2){
        return redisTemplate.opsForSet().union(key1 , key2) ;
    }

    /**
     * SUNIONSTORE destination key1 [key2] 所有给定集合的并集存储在 destination 集合中
     * @param key
     * @param key1
     * @param key2
     * @return
     */
    public Long sunionAndStore(String key, String key1 , String key2){
        return redisTemplate.opsForSet().unionAndStore(key1, key2, key) ;
    }
}
