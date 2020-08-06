package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.debug("This is a debug message from controller");
        logger.info("This is an info message from controller");
        logger.warn("This is a warn message from controller");
        logger.error("This is an error message from controller");
        new SpringLoggingHelper().helpMethod();
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
