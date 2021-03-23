package org.game.core.transport.node;

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
import org.game.core.ServiceNode;
import org.game.core.transport.ExchangeCodec;
import org.game.core.transport.ServerHandler;
import org.game.core.transport.TransportConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NodeServer {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(NodeServer.class);

    private final NioEventLoopGroup group = new NioEventLoopGroup();
    /** 服务端启动器 */
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    public NodeServer(ServiceNode serviceNode) {
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(TransportConsts.MAX_FRAME_LENGTH, 0, TransportConsts.HEAD_LENGTH_FIELD_LENGTH))
                                .addLast("hessianCodec", new ExchangeCodec())
                                .addLast("server-idle-handler", new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS))
                                .addLast("handler", new ServerHandler(serviceNode));
                    }
                });
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    /**
     * 启动node服务端监听
     * @param port 启动端口
     * @return future对象
     */
    public ChannelFuture start(int port) {
        final ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port));
        future.addListener(f -> {
            if (f.isSuccess()) {
                logger.info("Server bind." + serverBootstrap.config().localAddress());
            } else {
                logger.error("bind attempt failed.");
            }
        });
        return future;
    }

}