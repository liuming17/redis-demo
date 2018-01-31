package com.redis.demo.set;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class SetDemoServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private SetDemoService setDemoService ;

    @Test
    public void add() throws Exception {
        for(int i = 0 ; i < 100 ; i ++){
            setDemoService.add("keySet1" , i) ;
        }
        for(int i = 50 ; i < 150 ; i ++){
            setDemoService.add("keySet2" , i) ;
        }
    }

    @Test
    public void sCard() throws Exception {
        logger.info("keySet1 count : {} " , setDemoService.sCard("keySet1"));
        logger.info("keySet2 count : {} " , setDemoService.sCard("keySet2"));
        logger.info("keySet3 count : {} " , setDemoService.sCard("keySet3"));
    }

    @Test
    public void sdiff() throws Exception {
        Set set = setDemoService.sdiff("keySet2" , "keySet1") ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void sidffAndSet() throws Exception {
        setDemoService.sidffAndSet("keySet3","keySet2" , "keySet1") ;
    }

    @Test
    public void sinter() throws Exception {
        Set set = setDemoService.sinter("keySet2" , "keySet1") ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void sinterAndSet() throws Exception {
        setDemoService.sinterAndSet("keySet4","keySet2" , "keySet1") ;
    }

    @Test
    public void sismember() throws Exception {
        logger.info("sismember : {} " , setDemoService.sismember("keySet2" , 51));
    }

    @Test
    public void smembers() throws Exception {
        Set set = setDemoService.smembers("keySet2") ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void smove() throws Exception {
        setDemoService.smove("keySet5" , 51 , "keySet2") ;
    }

    @Test
    public void spop() throws Exception {
        Object value = setDemoService.spop("keySet2") ;
        logger.info("value : {} " , value);
    }

    @Test
    public void srandmember() throws Exception {
        List list = setDemoService.srandmember("keySet4" , 10) ;
        for(Object value : list){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void srem() throws Exception {
        long count = setDemoService.srem("keySet4" , 30, 40, 50, 60, 70) ;
        logger.info("count : {} " , count);
    }

    @Test
    public void sunion() throws Exception {
        Set set = setDemoService.sunion("keySet2" , "keySet1") ;
        for(Object value : set){
            logger.info("value : {} " , value);
        }
    }

    @Test
    public void sunionAndStore() throws Exception {
        setDemoService.sunionAndStore("keySet5","keySet2" , "keySet1") ;
    }

}