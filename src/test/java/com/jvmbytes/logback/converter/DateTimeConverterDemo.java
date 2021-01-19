package com.jvmbytes.logback.converter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

public class DateTimeConverterDemo {

    public static void main(String[] args) {
        DateTimeConverter.initialDefaultDateTimeConverter();
        Logger logger = createLoggerFor("dateTimeLogger");
        logger.info("test");
        logger.info("hello");
    }

    private static Logger createLoggerFor(String name) {
        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setEncoder(ple);
        appender.setContext(lc);
        appender.start();

        Logger logger = (Logger)LoggerFactory.getLogger(name);
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }
}
