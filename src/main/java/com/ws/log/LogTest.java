package com.ws.log;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.LoggingMXBean;

/**
 * jdk自带log
 * <p>
 * 几个注意点:
 * LogManager
 * 加载日志对应的配置文件,在jdk目录下 D:\jdk_1.8\jre\lib\logging.properties
 *
 * 最顶层有一个RootLogger
 */
public class LogTest {
    public static void main(String[] args) {
        // 可以通过JMX对指定的LOG名次的级别进行修改
        LoggingMXBean loggingMXBean = LogManager.getLoggingMXBean();
        LogManager logManager = LogManager.getLogManager();
        Logger logger = Logger.getLogger(LogTest.class.getSimpleName());
        logger.info("HelloWorld");
    }
}
