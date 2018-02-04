package com.cncnc.netty.server.server;

import com.gome.ad.ansp.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 服务主程序
 */
@Component
public class Server {
    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ApplicationContext applicationContext;

    public void init()  {
        final SslContext sslContext = null;


        EventLoopGroup bossGroup = null;
        if (serverConfig.getBossGroupCount() == 0) {
            bossGroup = new NioEventLoopGroup(1);
        } else {
            bossGroup = new NioEventLoopGroup(serverConfig.getBossGroupCount());
        }
        EventLoopGroup workerGroup = null;
        if (serverConfig.getWorkerGroupCount() == 0) {
            workerGroup = new NioEventLoopGroup();
        } else {
            workerGroup = new NioEventLoopGroup(serverConfig.getWorkerGroupCount());
        }
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(loadServerChannelInitializer());

            // Bind and start to accept incoming connections.
            ChannelFuture f = serverBootstrap.bind(serverConfig.getPort()).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    private ServerChannelInitializer loadServerChannelInitializer() {
        Class initializerClass = null;
        try {
            if (serverConfig.getChannelInitializerClass() != null) {
                initializerClass = Class.forName(serverConfig.getChannelInitializerClass());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (initializerClass == null) {
            initializerClass = DefaultChannelInitializer.class;
        }
        ServerChannelInitializer initializer =
                (ServerChannelInitializer)applicationContext.getAutowireCapableBeanFactory().getBean(initializerClass);
        return initializer;
    }

}
