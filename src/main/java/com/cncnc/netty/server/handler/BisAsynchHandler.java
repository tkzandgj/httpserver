package com.cncnc.netty.server.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.function.Consumer;

/**
 * 同步方式的业务处理handler
 */
public interface BisAsynchHandler {
    void handle(FullHttpRequest fullHttpRequest, Consumer<FullHttpResponse> successCallback, Consumer<Exception> failCallback);
}