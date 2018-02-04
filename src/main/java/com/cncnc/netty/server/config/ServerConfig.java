package com.cncnc.netty.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 服务端配置
 */
@Configuration
@ConfigurationProperties(prefix = "ansp.server")
@PropertySource("ansp.yml")
public class ServerConfig {
    private int port = 8080;

    private String encode = "UTF-8";

    private int bossGroupCount = 0;

    private int workerGroupCount = 0;

    private int maxRequestLength = 1048576;

    private String channelInitializerClass;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public int getBossGroupCount() {
        return bossGroupCount;
    }

    public void setBossGroupCount(int bossGroupCount) {
        this.bossGroupCount = bossGroupCount;
    }

    public int getWorkerGroupCount() {
        return workerGroupCount;
    }

    public void setWorkerGroupCount(int workerGroupCount) {
        this.workerGroupCount = workerGroupCount;
    }

    public int getMaxRequestLength() {
        return maxRequestLength;
    }

    public void setMaxRequestLength(int maxRequestLength) {
        this.maxRequestLength = maxRequestLength;
    }

    public String getChannelInitializerClass() {
        return channelInitializerClass;
    }

    public void setChannelInitializerClass(String channelInitializerClass) {
        this.channelInitializerClass = channelInitializerClass;
    }
}
