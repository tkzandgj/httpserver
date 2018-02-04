package com.cncnc.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

/**
 * 默认的回显handler，回显参数和body数据
 */
public class DefaultEchoHandler extends AnspChannelInboundHandler {
    @Override
    public void doChannelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest)msg;
            //解析url
            String url = httpRequest.uri();
            //QueryStringDecoder decoder = new QueryStringDecoder(url);
            //Map<String, List<String>> params = decoder.parameters();
            String body = httpRequest.content().toString(Charset.forName("UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            sb.append("<br>");
            sb.append(body);
            byte[] responseMsg = sb.toString().getBytes();
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, responseMsg.length);
            response.content().writeBytes(responseMsg);
            ctx.writeAndFlush(response);
        }
    }
}
