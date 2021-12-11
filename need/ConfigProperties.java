package io.pivotal.gemfirejmsprovider;

import java.util.Properties;

public class ConfigProperties {

    private Properties prop;
    static private final String FFMQ_BASE = "/Users/kdunn/javaStuff/ffmq4-distribution-4.0.0";

    public Properties get() {
        return prop;
    }

    public ConfigProperties() {
        prop = new Properties();

        prop.setProperty("management.destinationDefinitions.directory", FFMQ_BASE + "/destinations");
        prop.setProperty("management.bridgeDefinitions.directory", FFMQ_BASE + "/bridges");
        prop.setProperty("management.templates.directory", FFMQ_BASE + "/conf/templates");
        prop.setProperty("management.templates.mapping", FFMQ_BASE + "/conf/templates.mapping");
        prop.setProperty("management.autoCreate.queues", "true");
        prop.setProperty("management.autoCreate.topics", "true");
        prop.setProperty("management.deployOnStartup.queues", "true");
        prop.setProperty("management.deployOnStartup.topics", "true");
        prop.setProperty("management.remoteAdmin.enabled", "true");
        prop.setProperty("management.defaultData.directory", FFMQ_BASE + "/data");
        prop.setProperty("management.jmx.agent.enabled", "true");
        prop.setProperty("management.jmx.agent.jndi.rmi.port", "10003");
        prop.setProperty("management.jmx.agent.rmi.listenAddr", "0.0.0.0");
        prop.setProperty("listener.auth.timeout", "5");
        prop.setProperty("listener.tcp.enabled", "true");
        prop.setProperty("listener.tcp.useNIO", "false");
        prop.setProperty("listener.tcp.listenAddr", "0.0.0.0");
        prop.setProperty("listener.tcp.listenPort", "10002");
        prop.setProperty("listener.tcp.backLog", "50");
        prop.setProperty("listener.tcp.capacity", "200");
        prop.setProperty("transport.tcp.pingInterval", "30");
        prop.setProperty("transport.tcp.sendQueueMaxSize", "1000");
        prop.setProperty("transport.tcp.packet.maxSize", "1049600");
        prop.setProperty("transport.tcp.stream.sendBufferSize", "8192");
        prop.setProperty("transport.tcp.stream.recvBufferSize", "8192");
        prop.setProperty("transport.tcp.initialPacketBufferSize", "4096");
        prop.setProperty("transport.tcp.socket.sendBufferSize", "65536");
        prop.setProperty("transport.tcp.socket.recvBufferSize", "65536");
        prop.setProperty("transport.tcp.ssl.enabled", "false");
        prop.setProperty("transport.tcp.ssl.protocol", "SSLv3");
        prop.setProperty("transport.tcp.ssl.keyManager.algorithm", "SunX509");
        prop.setProperty("transport.tcp.ssl.keyStore.type", "JKS");
        prop.setProperty("transport.tcp.ssl.keyStore.path", FFMQ_BASE + "/conf/server-keystore.jks");
        prop.setProperty("transport.tcp.ssl.keyStore.password", "ffmqpass");
        prop.setProperty("transport.tcp.ssl.keyStore.keyPassword", "ffmqpass");
        prop.setProperty("security.enabled", "false");
        prop.setProperty("security.connector", "net.timewalker.ffmq4.security.XMLSecurityConnector");
        prop.setProperty("security.connector.xml.securityFile", FFMQ_BASE + "/conf/security.xml");
        prop.setProperty("asyncTaskManager.notification.threadPool.minSize", "5");
        prop.setProperty("asyncTaskManager.notification.threadPool.maxIdle", "10");
        prop.setProperty("asyncTaskManager.notification.threadPool.maxSize", "15");
        prop.setProperty("asyncTaskManager.delivery.threadPool.minSize", "0");
        prop.setProperty("asyncTaskManager.delivery.threadPool.maxIdle", "5");
        prop.setProperty("asyncTaskManager.delivery.threadPool.maxSize", "10");
        prop.setProperty("asyncTaskManager.diskIO.threadPool.minSize", "1");
        prop.setProperty("asyncTaskManager.diskIO.threadPool.maxIdle", "2");
        prop.setProperty("asyncTaskManager.diskIO.threadPool.maxSize", "4");
        prop.setProperty("consumer.prefetch.size", "10");
        prop.setProperty("delivery.redeliveryDelay", "0");
        prop.setProperty("log4j.logger.net.timewalker.ffmq4", "INFO,logFile");
        prop.setProperty("log4j.additivity.net.timewalker.ffmq4", "false");
        prop.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender ");
        prop.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.console.layout.ConversionPattern", "[%5p] %m%n");
        prop.setProperty("log4j.appender.logFile", "org.apache.log4j.RollingFileAppender");
        prop.setProperty("log4j.appender.logFile.File", FFMQ_BASE + "/logs/ffmq-server.log");
        prop.setProperty("log4j.appender.logFile.MaxFileSize", "1000KB");
        prop.setProperty("log4j.appender.logFile.MaxBackupIndex", "5");
        prop.setProperty("log4j.appender.logFile.Append", "true");
        prop.setProperty("log4j.appender.logFile.layout", "org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.logFile.layout.ConversionPattern", "%d{HHmmss} %5p [%c{1}]{%t} %m%n");

    }

}
