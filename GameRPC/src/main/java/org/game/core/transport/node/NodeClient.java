package org.game.core.transport.node;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.game.core.transport.ClientHandler;
import org.game.core.transport.ExchangeCodec;
import org.game.core.transport.TransportConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class NodeClient {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(NodeClient.class);

    private final String name;

    private final NioEventLoopGroup group;
    private final Bootstrap bootstrap;
    private ChannelFuture channelFuture;

    public NodeClient(String name) {
        this.name = name;
        group = new NioEventLoopGroup(1);
        this.bootstrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(TransportConsts.MAX_FRAME_LENGTH, 0, TransportConsts.HEAD_LENGTH_FIELD_LENGTH))
                                .addLast("hessianCodec", new ExchangeCodec())
                                .addLast("server-idle-handler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                                .addLast("handler", new ClientHandler());
                    }
                });
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    /**
     * 启动客户端node，连接服务端node
     * @param ip 服务端node的ip
     * @param port 服务端node的port
     * @return future对象
     */
    public void connect(String ip, int port) {
        channelFuture = bootstrap.clone().connect(new InetSocketAddress(ip, port));
        channelFuture.addListener(f -> {
            if (f.isSuccess()) {
                logger.info("Connection established." + bootstrap.config().remoteAddress());
            } else {
                logger.error("Connection attempt failed.");
            }
        });
    }

    /**
     * 发送数据
     * @param obj 要发送的对象
     */
    public void send(Object obj) {
        final Channel channel = channelFuture.channel();
        if (channel.isActive()) {
            channel.writeAndFlush(obj);
        } else {
            logger.warn("node连接断开。name = {}", name);
        }
    }

    /**
     * 空的node对象
     */
    public static class EmptyNodeClient extends NodeClient {

        public EmptyNodeClient(String name) {
            super(name);
        }

        @Override
        public void shutdown() {
            logger.trace("使用EmptyNodeClient对象shutdown");
        }

        @Override
        public void connect(String ip, int port) {
            logger.trace("使用EmptyNodeClient对象连接node");
        }

        @Override
        public void send(Object obj) {
            logger.trace("使用EmptyNodeClient对象发送数据");
        }
    }
}
