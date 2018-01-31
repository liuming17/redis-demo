package com.redis.demo.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuming on 2018/1/25.
 */
@Service
public class StringDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    /**
     * 设置值
     * set key hello
     * @param key
     * @param value
     */
    public void set(String key , String value){
        stringRedisTemplate.opsForValue().set(key , value);
    }

    /**
     * 设置值 set key hello
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    public void set(String key , String value , long time , TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key , value, time, timeUnit);
    }

    /**
     * 批量设置值
     * @param batchKeyValueMap
     */
    public void multiSet(Map<String , String> batchKeyValueMap){
        stringRedisTemplate.opsForValue().multiSet(batchKeyValueMap);
    }

    /**
     * 只有key 不存在时 才进行设置
     * SETNX key value
     * @param key
     * @param value
     */
    public void setNX(String key , String value){
        boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.setNX(key.getBytes(), value.getBytes());
                return true;
            }
        });
    }

    /**
     * 获取值
     * get key
     * @param key
     * @return
     */
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key) ;
    }

    /**
     * MGET key1 [key2..] 获取所有(一个或多个)给定 key 的值
     * @param keys
     * @return
     */
    public List<String> multiGet(List<String> keys){
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys) ;
        return values ;
    }

    /**
     * 递增
     * incr key 将 key 中储存的数字值增一
     * INCRBY key increment 将 key 所储存的值加上给定的增量值（increment）
     * INCRBYFLOAT key increment  将 key 所储存的值加上给定的浮点增量值（increment）
     * @param key
     * @return
     */
    public long incr(String key){
        return stringRedisTemplate.opsForValue().increment(key, 1) ;
    }

    /**
     * 递减
     * incr key 将 key 中储存的数字值减一
     * DECRBY key decrement key 所储存的值减去给定的减量值（decrement）
     * @param key
     * @return
     */
    public long decr(String key){
        return stringRedisTemplate.opsForValue().increment(key , -1) ;
    }

    /**
     * APPEND key value
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 指定value 追加到改 key 原来的值（value）的末尾
     * 如果 key 不存在 等同于 set key value
     * 如果 key 对应的value 不是一个字符串 则
     * @param key
     * @param value
     */
    public void append(String key , String value){
        stringRedisTemplate.opsForValue().append(key , value) ;
    }

    /**
     * GETRANGE key start end 返回 key 中字符串值的子字符
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getrange(String key , int start , int end){
        return stringRedisTemplate.opsForValue().get(key, start, end) ;
    }

    /**
     * GETSET key value
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     * @param key
     * @param newValue
     * @return
     */
    public String getSet(String key , String newValue){
        String oldValue = stringRedisTemplate.opsForValue().getAndSet(key , newValue) ;
        return oldValue ;
    }

    /**
     * 	GETBIT key offset
     * @param key
     * @param offset
     */
    public Boolean getBit(String key , int offset){
        Boolean result = stringRedisTemplate.opsForValue().getBit(key , offset) ;
        return result ;
    }

    /**
     * DEL key
     * 该命令用于在 key 存在时删除 key
     * @param key
     */
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * EXISTS key
     * 检查给定 key 是否存在。
     * @param key
     * @return
     */
    public Boolean exists(String key){
        return stringRedisTemplate.hasKey(key) ;
    }

    /**
     * EXPIRE key seconds   为给定 key 设置过期时间
     * EXPIREAT key timestamp EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     * PEXPIRE key milliseconds 设置 key 的过期时间以毫秒计。
     * @param key
     * @param timeout
     * @param timeUnit
     */
    public void expire(String key , long timeout , TimeUnit timeUnit){
        stringRedisTemplate.expire(key , timeout , timeUnit) ;
    }

    /**
     * KEYS pattern 查找所有符合给定模式( pattern)的 key
     * @param pattern
     * @return
     */
    public Set<String> keysPattern(String pattern){
        Set<String> keysSet = stringRedisTemplate.keys(pattern) ;
        return keysSet ;
    }

    /**
     * 	STRLEN key  返回 key 所储存的字符串值的长度
     * @param key
     * @return
     */
    public Long strLen(String key){
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.strLen(key.getBytes()) ;
            }
        });
    }

    /**
     * ping
     * @return
     */
    public String ping(){
        return stringRedisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
    }

    /**
     * dbsize 则是当前库key的数量
     * @return
     */
    public Long getDbsize(){
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize() ;
            }
        });
    }

    /**
     * 分页获取KEY 集
     * @param pattern
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<String> keysPagePattern(String pattern , int pageNum, int pageSize){

        /*ScanOptions options = ScanOptions.scanOptions().count(pageNum * pageSize).match(pattern).build();
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection rc = factory.getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<String>(pageSize);

        while(cursor.hasNext()){
            String key = new String(cursor.next()) ;
            logger.info("key : {} " , key);
            result.add(key) ;
        }*/
        return null ;
    }

}
