package com.redis.demo.hash;

import com.redis.demo.utils.RedisByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liuming on 2018/1/27.
 */
@Service
public class HashDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private RedisTemplate<String , Map<String , Object>> redisTemplate ;

    /**
     * HEXISTS key field 查看哈希表 key 中，指定的字段是否存在
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key , String field){
        /*return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(index);
                return redisConnection.hExists(key.getBytes() , field.getBytes());
            }
        }) ;*/
        return redisTemplate.opsForHash().hasKey(key , field) ;
    }

    /**
     * HSET key field value 将哈希表 key 中的字段 field 的值设为 value
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key , String field , Object value){
        redisTemplate.opsForHash().put(key , field , value);
    }

    /**
     * 	HSETNX key field value 只有在字段 field 不存在时，设置哈希表字段的值。
     * @param key
     * @param field
     * @param value
     */
    public void hsetNX(String key , String field , Object value){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                //需要指定序列化
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer() ;
                redisConnection.hSetNX(genericJackson2JsonRedisSerializer.serialize(key) ,
                        genericJackson2JsonRedisSerializer.serialize(field) , genericJackson2JsonRedisSerializer.serialize(value)) ;
                return true;
            }
        });
    }

    /**
     * 	HGET key field 获取存储在哈希表中指定字段的值。
     * @param key
     * @param field
     * @return
     */
    public Map<String , String> hget(String key , String field){
        return (Map<String , String>)redisTemplate.opsForHash().get(key, field) ;
    }

    /**
     * HGETALL key 获取在哈希表中指定 key 的所有字段和值
     * @param key
     * @return
     */
    public Map<String , Map<String , String>> getAll(String key){
        return redisTemplate.execute(new RedisCallback<Map<String , Map<String , String>>>() {
            @Override
            public Map<String , Map<String , String>> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer() ;
                Map<String , Map<String , String>> result = new HashMap<>() ;

                Map<byte[] , byte[]> map = redisConnection.hGetAll(genericJackson2JsonRedisSerializer.serialize(key)) ;
                if(map != null){
                    for(Map.Entry<byte[] , byte[]> entry : map.entrySet()){
                        String field = genericJackson2JsonRedisSerializer.deserialize(entry.getKey()).toString() ;
                        Map<String , String> mapValue = genericJackson2JsonRedisSerializer.deserialize(entry.getValue() , HashMap.class) ;
                        result.put(field , mapValue) ;
                    }
                }
                return result;
            }
        });
    }

    /**
     * 递增
     * HINCRBY key field increment
     * HINCRBYFLOAT key field increment
     * @param key
     * @param field
     * @param incr
     * @return
     */
    public Long incr(String key , String field, Long incr){
        return redisTemplate.opsForHash().increment(key , field , incr) ;
    }

    /**
     * HKEYS key 获取所有哈希表中的字段
     * @param key
     * @return
     */
    public Set<String> hkeys(String key){
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Set<String> result = new HashSet<>() ;
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer() ;
                Set<byte[]> set = redisConnection.hKeys(genericJackson2JsonRedisSerializer.serialize(key));
                for(byte[] field : set){
                    result.add(genericJackson2JsonRedisSerializer.deserialize(field).toString()) ;
                }
                return result ;
            }
        });
    }

    /**
     * 	HLEN key 获取哈希表中字段的数量
     * @param key
     * @return
     */
    public Long hlen(String key){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer() ;
                return redisConnection.hLen(genericJackson2JsonRedisSerializer.serialize(key));
            }
        });
    }

    /**
     * HMGET key field1 [field2] 获取所有给定字段的值
     * @param key
     * @param field
     * @return
     */
    public List<Map<String, String>> hmultiGet(String key , List field){
        return redisTemplate.opsForHash().multiGet(key , field) ;
    }

    /**
     * HMSET key field1 value1 [field2 value2 ] 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * @param key
     * @param values
     */
    public void hmultiSet(String key , Map<String , Object> values){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<byte[] , byte[]> redisValue = new HashMap<>() ;
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer() ;
                if(values != null){
                    for(Map.Entry<String , Object> entry : values.entrySet()){
                        if(entry.getValue() != null){
                            redisValue.put(genericJackson2JsonRedisSerializer.serialize(entry.getKey()) ,
                                    genericJackson2JsonRedisSerializer.serialize(entry.getValue())) ;
                        }
                    }
                }
                redisConnection.hMSet(genericJackson2JsonRedisSerializer.serialize(key) , redisValue);
                return true ;
            }
        });
    }

}
