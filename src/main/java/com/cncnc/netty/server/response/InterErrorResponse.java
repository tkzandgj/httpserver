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
public class InterErrorResponse extends DefaultFullHttpResponse {
	private static Logger logger = LoggerFactory.getLogger(InterErrorResponse.class);

	public InterErrorResponse(String msg)  {
		super(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
				Unpooled.buffer().writeBytes(msg.getBytes()));
		this.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.getBytes().length);
	}

	public InterErrorResponse()  {
		super(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		this.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
	}
}
