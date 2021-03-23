package org.game.core.transport;


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
import org.game.core.exchange.Request;
import org.game.core.exchange.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class RpcClient {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public void start(){
        final Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup())
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
        final ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8088));
        future.addListener(f -> {
            if (f.isSuccess()) {
                logger.info("Connection established.");

                final Channel channel = future.channel();

                channel.writeAndFlush("jack");

                for (int i = 0; i < 3; i++) {
                    final Request request = new Request(Request.allocId());
                    channel.writeAndFlush(request);
                }

                for (int i = 0; i < 3; i++) {
                    final Response request = new Response(i+10L, 20);
                    channel.writeAndFlush(request);
                }

                channel.writeAndFlush("end");
            } else {
                logger.error("Connection attempt failed.");
            }
        });

    }
}
