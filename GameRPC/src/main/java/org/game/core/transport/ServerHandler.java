package org.game.core.transport;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelDuplexHandler {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);

        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.write");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.channelActive channel = {}", ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.channelInactive");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.channelRead");
            logger.trace("msg type = {}, msg = {}", msg.getClass().getName(), msg);
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.exceptionCaught e = {}", cause.getMessage());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        if (logger.isTraceEnabled()) {
            logger.trace("ServerHandler.userEventTriggered evt = {}", evt);
        }
    }
}