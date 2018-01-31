package com.redis.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuming on 2018/1/30.
 */
public class MessageReceived {

    private Logger logger = LoggerFactory.getLogger(getClass()) ;

    public void receiveMessage(String message) {
        logger.info("Received <" + message + ">");
    }

}
