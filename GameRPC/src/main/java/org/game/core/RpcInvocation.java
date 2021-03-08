package org.game.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

public class RpcInvocation implements Serializable {

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
        final ServiceNode serviceNode = ServicePort.getServiceNode();
        serviceNode.addRpcInvocation(buffer);

        return true;
    }

    /**
     * 编码
     * @return 用于传输的编码后的数据
     * @throws IOException 失败
     */
    public byte[] encode() throws IOException {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final Hessian2Output hessian2Output = new Hessian2Output(bout);
        hessian2Output.writeObject(this);
        hessian2Output.flush();
        return bout.toByteArray();
    }

    /**
     * 解码
     * @param buffer 待解码的字符串
     * @return 解码成功返回 {@code true}
     * @throws IOException 失败
     */
    public static RpcInvocation decode(byte[] buffer) throws IOException {
        final ByteArrayInputStream bin = new ByteArrayInputStream(buffer);
        final Hessian2Input hessian2Input = new Hessian2Input(bin);
        return (RpcInvocation)hessian2Input.readObject();
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }
}
