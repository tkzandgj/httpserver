package com.cncnc.netty.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 业务处理逻辑配置
 */
@Configuration
@ConfigurationProperties(prefix = "ansp.bis.handler")
public class BisHandlerConfig {
    private List<BisHandlerConfigItem> handlers;

    public List<BisHandlerConfigItem> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<BisHandlerConfigItem> handlers) {
        this.handlers = handlers;
    }
}
