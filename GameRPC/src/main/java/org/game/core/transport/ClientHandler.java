package org.game.core.transport;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.game.core.exchange.Request;
import org.game.core.exchange.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ChannelDuplexHandler {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.write");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.channelActive channel = {}", ctx.channel());
        }

        ctx.channel().writeAndFlush("jack");

        for (int i = 0; i < 3; i++) {
            final Request request = new Request(Request.allocId());
            ctx.channel().writeAndFlush(request);
        }

        for (int i = 0; i < 3; i++) {
            final Response request = new Response(i+10L, 20);
            ctx.channel().writeAndFlush(request);
        }

        ctx.channel().writeAndFlush("end");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.channelInactive");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.channelRead");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.exceptionCaught e = {}", cause.getMessage());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        if (logger.isTraceEnabled()) {
            logger.trace("ClientHandler.userEventTriggered evt = {}", evt);
        }
    }
}
