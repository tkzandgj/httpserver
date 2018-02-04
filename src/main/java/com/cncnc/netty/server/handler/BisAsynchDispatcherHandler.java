package com.cncnc.netty.server.handler;

import com.cncnc.netty.server.config.BisHandlerConfig;
import com.cncnc.netty.server.config.BisHandlerConfigItem;
import com.cncnc.netty.server.response.InterErrorResponse;
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
import java.util.function.Consumer;

/**
 * player所有请求的handler
 */
@ChannelHandler.Sharable
@Component
@Lazy(true)
public class BisAsynchDispatcherHandler extends AnspChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(BisAsynchDispatcherHandler.class);

    @Autowired
    private BisHandlerConfig bisHandlerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, BisAsynchHandler> bisHandlerMap = new HashMap<>();

    @PostConstruct
    private void init() throws ClassNotFoundException {
        if (bisHandlerConfig.getHandlers() != null && bisHandlerConfig.getHandlers().size() > 0) {
            BeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
            for (BisHandlerConfigItem handlerConfigItem : bisHandlerConfig.getHandlers()) {
                Class handlerClass = Class.forName(handlerConfigItem.getHandlerClass());
                BisAsynchHandler handler = (BisAsynchHandler) beanFactory.getBean(handlerClass);
                bisHandlerMap.put(handlerConfigItem.getPath(), handler);
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

            BisAsynchHandler bisHandler = bisHandlerMap.get(path);

            if (bisHandler == null) {
                logger.error("{} unrecognized", path);
                channelHandlerContext.writeAndFlush(new NotFoundResponse());

            } else {
                bisHandler.handle(httpRequest, new Consumer<FullHttpResponse>() {
                    @Override
                    public void accept(FullHttpResponse response) {
                        channelHandlerContext.writeAndFlush(response);
                    }
                }, new Consumer<Exception>() {
                    @Override
                    public void accept(Exception e) {
                        channelHandlerContext.writeAndFlush(new InterErrorResponse());
                    }
                });
            }
        }
        ReferenceCountUtil.release(o);
    }
}
