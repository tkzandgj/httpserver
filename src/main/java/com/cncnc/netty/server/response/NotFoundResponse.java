package com.cncnc.netty.server.response;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 内部错误（500）http response
 * @author wuxuefei
 */
public class NotFoundResponse extends DefaultFullHttpResponse {
	private static Logger logger = LoggerFactory.getLogger(NotFoundResponse.class);

	public NotFoundResponse(String msg)  {
		super(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
				Unpooled.buffer().writeBytes(msg.getBytes()));
		this.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.getBytes().length);
	}

	public NotFoundResponse()  {
		super(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
		this.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
	}
}
