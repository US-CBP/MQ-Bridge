package com.connector.qq;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class JmsRoute extends RouteBuilder {

    static final Logger log = LoggerFactory.getLogger(JmsRoute.class);

    @Override
    public void configure() throws Exception {

    }
}
