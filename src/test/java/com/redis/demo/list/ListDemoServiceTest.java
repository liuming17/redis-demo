package com.redis.demo.list;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class ListDemoServiceTest {

    private static Logger logger = LoggerFactory.getLogger(ListDemoServiceTest.class) ;

    @Autowired
    private ListDemoService listDemoService ;

    @Test
    public void lset()throws Exception{
        List<String> values = new ArrayList<>() ;
        values.add("aa");
        values.add("bb");
        values.add("cb");

        listDemoService.lset("keylist" , 1 , values);
    }


    @Test
    public void lpush() throws Exception {
        List<String> values = new ArrayList<>() ;
        values.add("aqq");
        values.add("bqq");
        values.add("cqq");
        listDemoService.lpush("keylist" , values);
    }

    @Test
    public void rpush()throws Exception{
        Random rand = new Random() ;
        for(int i = 0 ; i < 100 ; i ++){
            listDemoService.rpush("keylist2" , rand.nextInt(2));
        }
    }

    @Test
    public void blpop()throws Exception{
        logger.info("value : {} " , listDemoService.blpop("keylist2"));
    }

    @Test
    public void brpop()throws Exception{
        logger.info("value : {} " , listDemoService.rpop("keylist2"));
    }


    @Test
    public void lindex() throws Exception {
        logger.info("value : {} " , listDemoService.lindex("keylist" , 1L));
    }

    @Test
    public void brpopAndLeft()throws Exception{
        logger.info("value : {} " , listDemoService.brpopAndLeft("keylist" , "keylistnew"));
    }

    @Test
    public void llen()throws Exception{
        logger.info("value : {} " , listDemoService.llen("keylist"));
    }

    @Test
    public void lrange()throws Exception{
        List<List<String>> result = listDemoService.lrange("keylist" , 1 , 3) ;
        for(int i = 1 ; i <= 3 ; i ++){
            logger.info("key : {} , value : {} " , i - 1 , result.get(i - 1));
        }
    }

    @Test
    public void lpushX() throws Exception{
        List<String> values = new ArrayList<>() ;
        values.add("a11");
        values.add("b22");
        values.add("c33");
        values.add("d44");
        listDemoService.lpushX("keylist" , values);
        listDemoService.lpushX("keylist2" , values);
    }

    @Test
    public void rpushX() throws Exception{
        List<String> values = new ArrayList<>() ;
        values.add("a111");
        values.add("b222");
        values.add("c333");
        values.add("d444");
        listDemoService.rpushX("keylist" , values);
        listDemoService.rpushX("keylist2" , values);
    }

    @Test
    public void lrem()throws Exception{
        logger.info("count : {} " , listDemoService.lrem("keylist2" , 100 , 0)) ;
    }

    @Test
    public void ltrim()throws Exception{
        listDemoService.ltrim("keylist2" , 1 , 10);
    }

}