package org.game.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

public class RpcInvocation implements Serializable {

    /** 调用rpc的线程调用点信息 */
    private FromPoint fromPoint;
    /** 目标调用的rpc调用点信息 */
    private CallPoint callPoint;

    /** rpc调用的方法 */
    private String methodName;
    /** rpc调用参数列表 */
    private Object[] methodArgs;

    /** rpc方法 */
    private transient Method method;
    /** 返回值类型 */
    private transient Class<?> returnType;

    public RpcInvocation() {

    }

    public RpcInvocation(FromPoint fromPoint, CallPoint callPoint, Method method, Object[] methodArgs) {
        this.fromPoint = fromPoint;
        this.callPoint = callPoint;
        this.methodName = method.getName();
        this.methodArgs = methodArgs;
        // transient
        this.method = method;
        this.returnType = method.getReturnType();
    }

    public FromPoint getFromPoint() {
        return fromPoint;
    }

    public CallPoint getCallPoint() {
        return callPoint;
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

    public boolean isOneWay() {
        // 这里有点问题，rpc接收端会有问题
        return returnType != null ? returnType.equals(Void.class) : true;
    }

    public boolean isCompletableFuture() {
        // 这里有点问题，rpc接收端会有问题
        return returnType != null ? returnType.equals(CompletableFuture.class) : true;
    }
}
