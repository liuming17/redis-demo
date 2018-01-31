package com.redis.demo.string;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class StringDemoServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private StringDemoService stringDemoService ;

    @Test
    public void set() throws Exception {
        stringDemoService.set("key" , "value");
    }

    @Test
    public void multiSet()throws Exception{
        Map<String , String> batchMap = new LinkedHashMap<>(10000) ;
        for(int i = 0 ; i < 500000 ; i ++){
            batchMap.put("key" + i , "value" + i);

            if(batchMap.size() == 10000){
                stringDemoService.multiSet(batchMap);
                batchMap = new HashMap<>(10000) ;
            }
        }
        if(batchMap.size() > 0){
            stringDemoService.multiSet(batchMap);
        }
    }

    @Test
    public void setNX()throws Exception{
        stringDemoService.setNX("key" , "value1");
        stringDemoService.setNX("key1" , "value1");
    }

    @Test
    public void get() throws Exception {
        String value = stringDemoService.get("key") ;
        logger.info("key : {} " , value);
    }

    @Test
    public void multiGet()throws Exception{
        List<String> keys = new ArrayList<>() ;
        for(int i = 0 ; i < 10 ; i ++){
            keys.add("key" + i) ;
            keys.add("aasd" + i) ;
        }
        List<String> values = stringDemoService.multiGet(keys) ;
        for(int i = 0 ; i < keys.size() ; i ++){
            logger.info("key : {} , value : {} " , keys.get(i) , values.get(i));
        }
    }

    @Test
    public void incr() throws Exception {
        String key = "incrKey" ;
        for(int i = 0 ; i < 100 ; i ++){
            long value = stringDemoService.incr(key) ;
            logger.info("incr value : {} " , value);
        }
    }

    @Test
    public void decr() throws Exception{
        String key = "incrKey" ;
        for(int i = 0 ; i < 100 ; i ++){
            long value = stringDemoService.decr(key) ;
            logger.info("incr value : {} " , value);
        }
    }

    @Test
    public void append()throws Exception{
        stringDemoService.append("key2" , "value2");
        stringDemoService.append("key1" , "value1");
    }

    @Test
    public void getrange()throws Exception{
        String value = stringDemoService.getrange("key2" , 0 , 2) ;
        logger.info("key2 getrange : {} " , value);
    }

    @Test
    public void getSet()throws Exception{
        for(int i = 0 ; i < 10 ; i ++){
            String oldVlue = stringDemoService.getSet("key2" , "value2" + i) ;
            logger.info("oldVlue : {} , newValue : {} " , oldVlue , stringDemoService.get("key2"));
        }
    }

    @Test
    public void delete()throws Exception{
        stringDemoService.delete("key2");
    }

    @Test
    public void exist()throws Exception{
        logger.info("key1 : {} " , stringDemoService.exists("key1"));
        logger.info("key2 : {} " , stringDemoService.exists("key2"));
    }

    @Test
    public void expire()throws Exception{
        stringDemoService.set("key2" , "value2");
        stringDemoService.expire("key2" , 1 , TimeUnit.MINUTES);
    }

    @Test
    public void strLen(){
        logger.info("key length : {} " , stringDemoService.strLen("key2"));
    }

    @Test
    public void keysPattern()throws Exception{
        Set<String> keys = stringDemoService.keysPattern("key*") ;
        for(String key : keys){
            logger.info("key : {} " , key);
        }
    }

    @Test
    public void ping() throws Exception{
        logger.info("ping : {} " , stringDemoService.ping());
    }

    @Test
    public void getDbsize()throws Exception{
        logger.info("dbsize : {} " , stringDemoService.getDbsize());
    }

    @Test
    public void keysPagePattern()throws Exception{
        int page = 1 ;

        boolean isNext = true ;

        List<String> keyList = stringDemoService.keysPagePattern("key*" , page , 10 ) ;

    }

}