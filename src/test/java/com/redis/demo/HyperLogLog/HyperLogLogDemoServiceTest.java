package com.redis.demo.HyperLogLog;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class HyperLogLogDemoServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Autowired
    private HyperLogLogDemoService hyperLogLogDemoService ;

    @Test
    public void pfadd() throws Exception {
        Random random = new Random() ;
        for(int i = 0 ; i < 1000 ; i ++){
            hyperLogLogDemoService.pfadd("pfkey1" , random.nextInt(5));
            hyperLogLogDemoService.pfadd("pfkey2" , random.nextInt(5));
        }
    }

    @Test
    public void pfcount() throws Exception {
        logger.info("pfkey1 pfcount : {}" , hyperLogLogDemoService.pfcount("pfkey1"));
        logger.info("pfkey2 pfcount : {}" , hyperLogLogDemoService.pfcount("pfkey2"));
        logger.info("pfcount : {}" , hyperLogLogDemoService.pfcount("pfkey1","pfkey2"));
    }

    @Test
    public void pfmerge() throws Exception {
        hyperLogLogDemoService.pfmerge("pfkey3" , "pfkey1" , "pfkey2");
    }

}