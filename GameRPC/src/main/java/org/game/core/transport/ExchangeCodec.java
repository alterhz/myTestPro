package org.game.core.transport;

import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.game.core.exchange.Request;
import org.game.core.exchange.Response;
import org.game.core.exchange.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExchangeCodec extends ByteToMessageCodec {

    public static final byte FLAG_STRING = 0x10;
    public static final byte FLAG_REQUEST = 0x20;
    public static final byte FLAG_RESPONSE = 0x30;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ExchangeCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof String) {
            final String str = (String) msg;

            final int headIndex = out.writerIndex();
            out.writerIndex(headIndex + Consts.HEAD_LENGTH);

            int length = 0;
            final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            out.writeBytes(bytes);
            length += str.length();

            // 写入消息包头
            out.writerIndex(headIndex);
            out.writeInt(Consts.HEAD_LENGTH + length - Consts.HEAD_LENGTH_FIELD_LENGTH);
            out.writeByte(FLAG_STRING);
            out.writerIndex(headIndex + Consts.HEAD_LENGTH + length);
        } else if (msg instanceof Request) {
            final Request request = (Request) msg;

            final int headIndex = out.writerIndex();
            out.writerIndex(headIndex + Consts.HEAD_LENGTH);

            int length = 0;
            final byte[] encode = Utils.encode(request);
            out.writeBytes(encode);
            length += encode.length;

            // 写入消息包头
            out.writerIndex(headIndex);
            out.writeInt(Consts.HEAD_LENGTH + length - Consts.HEAD_LENGTH_FIELD_LENGTH);
            out.writeByte(FLAG_REQUEST);
            out.writerIndex(headIndex + Consts.HEAD_LENGTH + length);
        } else if (msg instanceof Response) {
            final Response response = (Response) msg;

            final int headIndex = out.writerIndex();
            out.writerIndex(headIndex + Consts.HEAD_LENGTH);

            int length = 0;
            final byte[] encode = Utils.encode(response);
            out.writeBytes(encode);
            length += encode.length;

            // 写入消息包头
            out.writerIndex(headIndex);
            out.writeInt(Consts.HEAD_LENGTH + length - Consts.HEAD_LENGTH_FIELD_LENGTH);
            out.writeByte(FLAG_RESPONSE);
            out.writerIndex(headIndex + Consts.HEAD_LENGTH + length);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        final int fieldLength = in.readInt();
        final byte flag = in.readByte();
        if (flag == FLAG_STRING) {
            final ByteBuf byteBuf = in.readBytes(fieldLength - 1);
            final String str = String.valueOf(byteBuf.toString(StandardCharsets.UTF_8));

            out.add(str);
            logger.trace("接收到消息包长度。fieldLength = {}, in.length = {}, str = {}", fieldLength, in.readableBytes(), str);
        } else if (flag == FLAG_REQUEST) {
            int length = fieldLength - Consts.HEAD_FLAG_LENGTH;
            byte[] buffer = new byte[length];
            in.readBytes(buffer);

            final Request request = Utils.decode(buffer);

            out.add(request);
            logger.trace("接收到消息包长度。fieldLength = {}, in.length = {}, request.id = {}", fieldLength, in.readableBytes(), request.getId());
        } else if (flag == FLAG_RESPONSE) {
            int length = fieldLength - Consts.HEAD_FLAG_LENGTH;
            byte[] buffer = new byte[length];
            in.readBytes(buffer);

            final Response response = Utils.decode(buffer);
            out.add(response);
            logger.trace("接收到消息包长度。fieldLength = {}, in.length = {}, response.id = {}", fieldLength, in.readableBytes(), response.getId());
        }
    }
}
