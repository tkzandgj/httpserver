package com.cncnc.netty.server.handler;

import com.cncnc.netty.server.config.BisHandlerConfig;
import com.cncnc.netty.server.config.BisHandlerConfigItem;
import com.cncnc.netty.server.response.NotFoundResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * player所有请求的handler
 */
@ChannelHandler.Sharable
@Component
@Lazy(true)
public class BisSyncDispatcherHandler extends AnspChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(BisSyncDispatcherHandler.class);

    @Autowired
    private BisHandlerConfig bisHandlerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, BisSyncHandler> adapterHandlerMap = new HashMap<>();

    @PostConstruct
    private void init() throws ClassNotFoundException {
        if (bisHandlerConfig.getHandlers() != null && bisHandlerConfig.getHandlers().size() > 0) {
            BeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
            for (BisHandlerConfigItem handlerConfigItem : bisHandlerConfig.getHandlers()) {
                Class adapterHandlerClass = Class.forName(handlerConfigItem.getHandlerClass());
                BisSyncHandler adapterHandler = (BisSyncHandler) beanFactory.getBean(adapterHandlerClass);
                adapterHandlerMap.put(handlerConfigItem.getPath(), adapterHandler);
            }
        }
    }


    @Override
    protected void doChannelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest)o;

            String uri = httpRequest.uri();
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            String path = decoder.path();

            BisSyncHandler bisSyncHandler = adapterHandlerMap.get(path);

            if (bisSyncHandler == null) {
                logger.error("{} unrecognized", path);
                channelHandlerContext.writeAndFlush(new NotFoundResponse());
            } else {
                FullHttpResponse response = bisSyncHandler.handle(httpRequest);
                channelHandlerContext.writeAndFlush(response);
            }
            ReferenceCountUtil.release(o);
        }
    }
}
