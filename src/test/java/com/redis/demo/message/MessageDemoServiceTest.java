package com.redis.demo.message;

import com.redis.demo.RedisDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by liuming on 2018/1/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= RedisDemoApplication.class)
public class MessageDemoServiceTest {

    @Autowired
    private RedisMessageListenerContainer container ;

    @Autowired
    private MessageDemoService messageDemoService ;

    @Test
    public void sendMessage() throws Exception {

        int count = 1 ;
        while(true){
            messageDemoService.sendMessage("sprinboot-redis-messaage" , "aa" + count);
            messageDemoService.sendMessage("sprinboot-redis-messaage1" , "bb" + count);
            if(count == 100){
                container.stop();
            }
            count ++ ;
        }
    }

}