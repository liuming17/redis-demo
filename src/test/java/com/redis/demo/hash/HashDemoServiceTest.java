package com.redis.demo.hash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by liuming on 2018/1/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class HashDemoServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private HashDemoService hashDemoService ;

    @Test
    public void hexists() throws Exception {
        logger.info("key1 , sex , : {} " , hashDemoService.hexists("key1" , "sex"));
    }

    @Test
    public void hset() throws Exception{
        Map<String , String> values = new HashMap<>() ;
        values.put("aa" , "aa") ;
        values.put("bb" , "bb") ;
        hashDemoService.hset("key" , "field1" , values);
        hashDemoService.hset("key" , "field2" , values);
    }

    @Test
    public void hget() throws Exception{
        Map<String , String> result = hashDemoService.hget("key" , "field1") ;
        logger.info("aa : {} , bb : {} " , result.get("aa") , result.get("bb"));
    }

    @Test
    public void hsetNX() throws Exception {
        Map<String , String> values = new HashMap<>() ;
        values.put("aa" , "aaaa") ;
        values.put("bb" , "bbba") ;
        hashDemoService.hsetNX("key" , "field2" , values);
        hashDemoService.hsetNX("key" , "field3" , values);
        hashDemoService.hsetNX("key" , "field4" , values);
    }

    @Test
    public void getAll() throws Exception {
        Map<String, Map<String , String>> values = hashDemoService.getAll("key") ;

        ObjectMapper objectMapper = new ObjectMapper() ;
        for(Map.Entry<String , Map<String, String>> entry : values.entrySet()){
            logger.info("key : {} , value : {} " , entry.getKey() , objectMapper.writeValueAsString(entry.getValue()));
        }
    }

    @Test
    public void incr() throws Exception {
        for(int i = 0 ; i < 100 ; i ++){
            logger.info("incr : {} " , hashDemoService.incr("key1" , "field" , 1L));
        }
    }

    @Test
    public void hkeys() throws Exception {
        Set<String> result = hashDemoService.hkeys("key") ;
        for(String field : result){
            logger.info("field : {} " , field);
        }
    }

    @Test
    public void hlen() throws Exception {
        logger.info("key length : {} " , hashDemoService.hlen("key"));
    }

    @Test
    public void hmultiGet() throws Exception {
        List<String> fieldList = new ArrayList<>() ;
        fieldList.add("field1");
        fieldList.add("field2");
        fieldList.add("field5");
        List<Map<String, String>> list = hashDemoService.hmultiGet("key" , fieldList) ;
        ObjectMapper objectMapper = new ObjectMapper() ;
        for(Map<String , String> result : list){
            logger.info("result : {} " , objectMapper.writeValueAsString(result));
        }
    }

    @Test
    public void hmultiSet() throws Exception {

        Map<String , String> values = new HashMap<>() ;
        values.put("aa" , "aa") ;
        values.put("bb" , "bb") ;

        Map<String , Object> fieldMap = new HashMap<>() ;
        fieldMap.put("field1" , values) ;
        fieldMap.put("field2" , values) ;
        fieldMap.put("field3" , values) ;

        hashDemoService.hmultiSet("key2" , fieldMap) ;
    }

}