package org.game.core.transport;

public class Consts {

    /** 单个消息包最大长度 */
    public static final int MAX_FRAME_LENGTH = 64 * 1024;
    /** 消息包头长度占用2字节 */
    public static final int HEAD_LENGTH_FIELD_LENGTH = 4;
    /** 消息包头标记位长度 */
    public static final int HEAD_FLAG_LENGTH = 1;
    /** 消息包头长度总长度 */
    public static final int HEAD_LENGTH = 5;

}
