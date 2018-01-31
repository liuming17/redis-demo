package com.redis.demo.set;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class SortedSetDemoServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private SortedSetDemoService sortedSetDemoService ;

    @Test
    public void add() throws Exception {
        Random random = new Random() ;
        for(int i = 0 ; i < 100 ; i ++){
            sortedSetDemoService.add("skey11", i, 1);
        }
        for(int i = 50 ; i < 150 ; i ++){
            sortedSetDemoService.add("skey22", i, 0);
        }
    }

    @Test
    public void zcard() throws Exception {
        logger.info("skey1 size : {} " , sortedSetDemoService.zcard("skey1"));
        logger.info("skey2 size : {} " , sortedSetDemoService.zcard("skey2"));
    }

    @Test
    public void zcount() throws Exception {
        logger.info("skey2 size : {} " , sortedSetDemoService.zcount("skey2", 0.5, 1));
    }

    @Test
    public void zincr() throws Exception {
        sortedSetDemoService.zincr("skey1" , 50 , 1);
    }

    @Test
    public void zinterstore() throws Exception {
        sortedSetDemoService.zinterstore("skey1" , "skey2" , "skey3");
    }

    @Test
    public void zrange() throws Exception {
        Set set = sortedSetDemoService.zrange("skey1" , 20, 30);
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void zrangeByLex() throws Exception {
        Set set = sortedSetDemoService.zrangeByLex("skey11" , 10, 20);
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void zrangeByScore() throws Exception {
        Set set = sortedSetDemoService.zrangeByScore("skey1" , 0.56, 1);
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void zrank() throws Exception {
        logger.info("zrank : {} " , sortedSetDemoService.zrank("skey1" , 50));
    }

    @Test
    public void zrem() throws Exception {
        sortedSetDemoService.zrem("skey11" , 92) ;
    }

    @Test
    public void zremRange() throws Exception {
        sortedSetDemoService.zremRange("skey11" , 10,20) ;
    }

    @Test
    public void zremRangeByScore() throws Exception {
        long count = sortedSetDemoService.zremRangeByScore("skey1" , 0.59 , 1) ;
        logger.info("count : {} " , count);
    }

    @Test
    public void zrevrange() throws Exception {
        Set set = sortedSetDemoService.zrevrange("skey1", 10, 20) ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void zrevrangeByScore() throws Exception {
        Set set = sortedSetDemoService.zrevrangeByScore("skey1", 0.56, 1) ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void zrevrank() throws Exception {
        logger.info("zrank : {} " , sortedSetDemoService.zrevrank("skey1" , 50));
    }

    @Test
    public void zsocre() throws Exception {
        logger.info("zrank : {} " , sortedSetDemoService.zsocre("skey1" , 50));
    }

    @Test
    public void unionAndStore() throws Exception {
        sortedSetDemoService.unionAndStore("skey1" , "skey2" , "skey12");
    }

}