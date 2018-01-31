package com.redis.demo.config;

import com.redis.demo.message.MessageReceived;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by liuming on 2018/1/25.
 */
@Configuration
public class RedisConfiguration {

    @Bean
    @ConditionalOnMissingBean(name="redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate<>()  ;
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate ;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate redisTemplate = new StringRedisTemplate()  ;
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate ;
    }

    /*
     * Redis消息监听器容器
     * 这个容器加载了RedisConnectionFactory和消息监听器
     */
    @Bean
    @ConditionalOnMissingBean(name="container")
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("sprinboot-redis-messaage"));
        return container;
    }

    @Bean
    @ConditionalOnMissingBean(name="container2")
    RedisMessageListenerContainer container2(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("sprinboot-redis-messaage1"));
        return container;
    }

    /**
     * 将Receiver注册为一个消息监听器，并指定消息接收的方法（receiveMessage）
     * 如果不指定消息接收的方法，消息监听器会默认的寻找Receiver中的handleMessage这个方法作为消息接收的方法
     * @param messageReceived
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceived messageReceived){
        return new MessageListenerAdapter(messageReceived, "receiveMessage");
    }

    /**
     * 消息接收类
     * @return
     */
    @Bean
    MessageReceived messageReceived(){
        return new MessageReceived() ;
    }

}
