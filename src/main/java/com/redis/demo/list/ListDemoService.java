package com.redis.demo.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuming on 2018/1/29.
 */
@Service
public class ListDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private RedisTemplate redisTemplate ;

    /**
     * LSET key index value 通过索引设置列表元素的值
     * @param key
     * @param values
     */
    public void lset(String key , long index , List<String> values){
        redisTemplate.opsForList().set(key , index , values);
    }

    /**
     * LPUSH key value1 [value2] 将一个或多个值插入到列表头部
     * @param key
     * @param values
     */
    public void lpush(String key , List<String> values){
        redisTemplate.opsForList().leftPush(key , values);
    }

    /**
     * LPUSHX key value 将一个值插入到已存在的列表头部
     * @param key
     * @param values
     */
    public void lpushX(String key , List<String> values){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer() ;
                redisConnection.lPushX(serializer.serialize(key) , serializer.serialize(values)) ;
                return true;
            }
        });
    }


    /**
     * RPUSH key value1 [value2] 在列表中添加一个或多个值
     * @param key
     * @param values
     */
    public void rpush(String key, Object values){
        redisTemplate.opsForList().rightPush(key , values);
    }

    /**
     * RPUSHX key value 为已存在的列表添加值
     * @param key
     * @param values
     */
    public void rpushX(String key , List<String> values){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer() ;
                redisConnection.rPushX(serializer.serialize(key) , serializer.serialize(values)) ;
                return true;
            }
        });
    }

    /**
     * LINDEX key index 通过索引获取列表中的元素
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, Long index){
        Object value = redisTemplate.opsForList().index(key , index) ;
        if(value != null){
            return value.toString() ;
        }
        return null ;
    }

    /**
     * LPOP key 移出并获取列表的第一个元素
     * @param key
     * @return
     */
    public List<String> lpop(String key){
        return (List<String>) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * BLPOP key1 [key2 ] timeout
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param key
     * @return
     */
    public List<String> blpop(String key){
        List<String> result = (List<String>) redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer() ;
                List<byte[]> tempResult = redisConnection.bLPop(0 , serializer.serialize(key)) ;
                List<String> result = new ArrayList<>() ;
                for(byte[] bytes : tempResult){
                    result.add(serializer.deserialize(bytes).toString()) ;
                }
                return result;
            }
        });
        return result ;
    }

    /**
     * 	RPOP key 移除并获取列表最后一个元素
     * @param key
     * @return
     */
    public List<String> rpop(String key){
        return (List<String>) redisTemplate.opsForList().rightPop(key);
    }


    /**
     * BRPOP key1 [key2 ] timeout
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param key
     * @return
     */
    public List<String> brpop(String key){
        return (List<String>) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 	BRPOPLPUSH source destination timeout
     * 	从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * 	RPOPLPUSH source destination 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     * @param key
     * @param newKey
     * @return
     */
    public List<String> brpopAndLeft(String key, String newKey){
        return (List<String>) redisTemplate.opsForList().rightPopAndLeftPush(key , newKey) ;
    }

    /**
     * LLEN key 获取列表长度
     * @param key
     * @return
     */
    public long llen(String key){
        return redisTemplate.opsForList().size(key) ;
    }

    /**
     * 	LRANGE key start stop 获取列表指定范围内的元素
     * 	LRANGE命令也支持负索引，表示从右边开始计算序数，如"−1"表示最右边第一个元素，"-2"表示最右边第二个元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<List<String>> lrange(String key , long start , long end){
        return redisTemplate.opsForList().range(key , start , end) ;
    }

    /**
     * LREM key count value 移除列表元素
     * count 为正数时  从左边开始删
     * count 为负数时  从右边开始删
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(String key , long count , Object value){
        return redisTemplate.opsForList().remove(key , count , value) ;
    }

    /**
     * LTRIM key start stop 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * @param key
     * @param start
     * @param end
     */
    public void ltrim(String key , long start , long end){
        redisTemplate.opsForList().trim(key, start , end);
    }

}
