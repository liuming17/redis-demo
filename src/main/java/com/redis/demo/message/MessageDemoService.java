package com.redis.demo.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by liuming on 2018/1/30.
 */
@Service
public class MessageDemoService {

    @Autowired
    private RedisTemplate redisTemplate ;

    public void sendMessage(String channel , String message){
        redisTemplate.convertAndSend(channel, message) ;
    }
}
