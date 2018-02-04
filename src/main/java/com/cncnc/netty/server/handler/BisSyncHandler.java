package com.cncnc.netty.server.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 同步方式的业务处理handler
 */
public interface BisSyncHandler {
    FullHttpResponse handle(FullHttpRequest fullHttpRequest);
}