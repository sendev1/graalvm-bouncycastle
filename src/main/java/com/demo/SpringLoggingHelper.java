package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringLoggingHelper {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public void helpMethod(){
        logger.debug("This is a debug message from helper");
        logger.info("This is an info message from helper");
        logger.warn("This is a warn message from helper");
        logger.error("This is an error message from helper");
    }
}
