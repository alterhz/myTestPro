package org.game.core.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class RpcServer {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public void start() {
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final ServerBootstrap bootstrap = serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Consts.MAX_FRAME_LENGTH, 0, Consts.HEAD_LENGTH_FIELD_LENGTH))
                                .addLast("hessianCodec", new ExchangeCodec())
                                .addLast("server-idle-handler", new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS))
                                .addLast("handler", new ServerHandler());
                    }
                });
        final ChannelFuture future = bootstrap.bind(new InetSocketAddress(8088));
        future.addListener(f -> {
            if (f.isSuccess()) {
                logger.info("Server bind.");
            } else {
                logger.error("bind attempt failed.");
            }
        });
    }

}
