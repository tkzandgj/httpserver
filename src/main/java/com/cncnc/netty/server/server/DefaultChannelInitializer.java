package com.cncnc.netty.server.server;

import com.gome.ad.ansp.config.ServerConfig;
import com.gome.ad.ansp.handle.DefaultEchoHandler;
import io.netty.channel.ChannelPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(true)
public class DefaultChannelInitializer extends ServerChannelInitializer {
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public int getMaxRequestLength() {
        return this.serverConfig.getMaxRequestLength();
    }

    @Override
    public void doChannelInit(ChannelPipeline p) {
        p.addLast(new DefaultEchoHandler());
    }
}
