package com.redis.demo.set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by liuming on 2018/1/30.
 */
@Service
public class SortedSetDemoService {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private RedisTemplate redisTemplate ;

    /**
     * ZADD key score1 member1 [score2 member2] 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     * @param key
     * @param value
     * @param score
     */
    public void add(String key, Object value, double score){
        redisTemplate.opsForZSet().add(key, value, score) ;
    }

    /**
     * ZCARD key 获取有序集合的成员数
     * @param key
     * @return
     */
    public Long zcard(String key){
        return redisTemplate.opsForZSet().zCard(key) ;
    }

    /**
     * ZCOUNT key min max 计算在有序集合中指定区间分数的成员数
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key , double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * ZLEXCOUNT key min max 在有序集合中计算指定字典区间内成员数量
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zlexcount(String key, Object min, Object max){
        return (Long)redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer redisSerializer = redisTemplate.getDefaultSerializer() ;

                RedisZSetCommands.Range range = new RedisZSetCommands.Range() ;
                range.gte(min) ;
                range.lte(max) ;
                Long count = redisConnection.zCount(redisSerializer.serialize(key) , range) ;
                return count;
            }
        });
    }

    /**
     * ZINCRBY key increment member 有序集合中对指定成员的分数加上增量 increment
     * @param key
     * @param value
     * @param score
     */
    public void zincr(String key, Object value, double score){
        redisTemplate.opsForZSet().incrementScore(key, value, score) ;
    }

    /**
     * ZINTERSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     * @param key1
     * @param key2
     * @param newKey
     */
    public void zinterstore(String key1, String key2, String newKey){
        redisTemplate.opsForZSet().intersectAndStore(key1, key2, newKey) ;
    }

    /**
     * ZRANGE key start stop [WITHSCORES] 通过索引区间返回有序集合成指定区间内的成员
     * @param key
     * @param min
     * @param max
     */
    public Set zrange(String key, long min, long max){
        return redisTemplate.opsForZSet().range(key, min, max) ;
    }

    /**
     * ZRANGEBYLEX key min max [LIMIT offset count] 通过字典区间返回有序集合的成员
     * 当有序集合的所有成员都具有相同的分值时， 有序集合的元素会根据成员的字典序（lexicographical ordering）来进行排序，
     * 而这个命令则可以返回给定的有序集合键 key 中， 值介于 min 和 max 之间的成员。
     * 如果有序集合里面的成员带有不同的分值， 那么命令返回的结果是未指定的
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set zrangeByLex(String key, Object min, Object max){
        RedisZSetCommands.Range range = new RedisZSetCommands.Range() ;
        range.gte(min) ;
        range.lte(max) ;
        return redisTemplate.opsForZSet().rangeByLex(key , range) ;
    }

    /**
     * 	ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT] 通过分数返回有序集合指定区间内的成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set zrangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScore(key, min, max) ;
    }

    /**
     * 	ZRANK key member 返回有序集合中指定成员的索引
     * @param key
     * @param value
     * @return
     */
    public Long zrank(String key, Object value){
        return redisTemplate.opsForZSet().rank(key, value) ;
    }

    /**
     * ZREM key member [member ...] 移除有序集合中的一个或多个成员
     * @param key
     * @param value
     * @return
     */
    public Long zrem(String key , Object value){
        return redisTemplate.opsForZSet().remove(key , value) ;
    }

    /**
     * ZREMRANGEBYLEX key min max 移除有序集合中给定的字典区间的所有成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zremRange(String key , long min , long max){
        return redisTemplate.opsForZSet().removeRange(key, min, max) ;
    }

    /**
     * ZREMRANGEBYSCORE key min max 移除有序集合中给定的分数区间的所有成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zremRangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max) ;
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES] 返回有序集中指定区间内的成员，通过索引，分数从高到底
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set zrevrange(String key, long start, long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 	ZREVRANGEBYSCORE key max min [WITHSCORES]  返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set zrevrangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max) ;
    }

    /**
     * ZREVRANK key member 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     * @param key
     * @param value
     * @return
     */
    public Long zrevrank(String key, Object value){
        return redisTemplate.opsForZSet().reverseRank(key , value) ;
    }

    /**
     * ZSCORE key member 返回有序集中，成员的分数值
     * @param key
     * @param value
     * @return
     */
    public Double zsocre(String key, Object value){
        return redisTemplate.opsForZSet().score(key, value) ;
    }

    /**
     * 	ZUNIONSTORE destination numkeys key [key ...] 计算给定的一个或多个有序集的并集，并存储在新的 key 中
     * @param key1
     * @param key2
     * @param newKey
     */
    public void unionAndStore(String key1, String key2, String newKey){
        redisTemplate.opsForZSet().unionAndStore(key1, key2, newKey) ;
    }

}
