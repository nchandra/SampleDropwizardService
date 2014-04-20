package com.nishant.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
Main Spring Configuration
 */
@Configuration
@ImportResource({ "classpath:spring/applicationContext.xml", "classpath:spring/dao.xml" })
@ComponentScan(basePackages = {"com.nishant.example"})
public class MyAppSpringConfiguration {

}
