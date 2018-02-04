package com.cncnc.netty.server.handler;


import com.cncnc.netty.server.response.Empty200Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ansp所有handler的基类
 */
public abstract class AnspChannelInboundHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AnspChannelInboundHandler.class);

	private static final Logger ioexceptionLogger = LoggerFactory.getLogger("ioexception");

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			doChannelRead(ctx, msg);
		} catch (Exception e) {
			ReferenceCountUtil.release(msg);
			throw e;
		} finally {
			//ReferenceCountUtil.release(msg);
		}
	}

	abstract protected void doChannelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			// most of it was caused by the health check of the load balance server
			// just ignore it
			// ioexceptionLogger.error("error", cause);
		} else {
			logger.error("remote address:{}", ctx.channel().remoteAddress().toString());
			logger.error("handle exception: ", cause);
		}
		FullHttpResponse response = new Empty200Response();
		ctx.writeAndFlush(response);
	}
}
