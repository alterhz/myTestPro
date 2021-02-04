package org.game.core;

import java.io.IOException;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

public class RpcInvocation {

    /** 调用rpc的线程调用点信息 */
    private final FromPoint fromPoint;
    /** 目标调用的rpc调用点信息 */
    private final CallPoint callPoint;

    /** rpc调用的方法 */
    private final String methodName;
    /** rpc调用参数列表 */
    private final Object[] methodArgs;

    //transient

    public RpcInvocation(FromPoint fromPoint, CallPoint callPoint, String methodName, Object[] methodArgs) {
        this.fromPoint = fromPoint;
        this.callPoint = callPoint;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
    }

    public FromPoint getFromPoint() {
        return fromPoint;
    }

    public CallPoint getCallPoint() {
        return callPoint;
    }

    public boolean invoke() throws IOException {
        final byte[] buffer = encode();

        // TODO code 要通过网络传输的数据

        return true;
    }

    /**
     * 编码
     * @return 用于传输的编码后的数据
     * @throws IOException 失败
     */
    public byte[] encode() throws IOException {
        final byte[] buffer = new byte[1024];
        final CodedOutputStream out = CodedOutputStream.newInstance(buffer);
        out.writeStringNoTag(fromPoint.getNode());
        out.writeStringNoTag(fromPoint.getPort());
        out.writeStringNoTag(callPoint.getNode());
        out.writeStringNoTag(callPoint.getPort());
        out.writeStringNoTag(callPoint.getService());

        return null;
    }

    /**
     * 解码
     * @param buffer 待解码的字符串
     * @return 解码成功返回 {@code true}
     * @throws IOException 失败
     */
    public static RpcInvocation decode(byte[] buffer) throws IOException {
        final CodedInputStream codedInputStream = CodedInputStream.newInstance(buffer);
        final String fromNode = codedInputStream.readString();
        final String fromPort = codedInputStream.readString();
        final String callNode = codedInputStream.readString();
        final String callPort = codedInputStream.readString();
        final String callService = codedInputStream.readString();
        final FromPoint fromPoint = new FromPoint(fromNode, fromPort);
        final CallPoint callPoint = new CallPoint(callNode, callPort, callService);

        final RpcInvocation rpcInvocation = new RpcInvocation(fromPoint, callPoint, "",
                new Object[]{});
        return rpcInvocation;
    }
}
