package org.game.core.refer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import org.game.core.CallPoint;
import org.game.core.FromPoint;
import org.game.core.RpcInvocation;
import org.game.core.ServiceConfig;
import org.game.core.ServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RemoteServiceInvoker<T> implements InvocationHandler {

    private final Class<T> type;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceInvoker.class);

    public RemoteServiceInvoker(Class<T> type) {
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, IOException {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            if ("toString".equals(methodName)) {
                return this.toString();
            } else if ("$destroy".equals(methodName)) {
                return null;
            } else if ("hashCode".equals(methodName)) {
                return this.hashCode();
            }
        } else if (parameterTypes.length == 1 && "equals".equals(methodName)) {
            return this.equals(args[0]);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("type = " + type.getName()
                    + ", methodName = " + methodName
                    + ", args = " + StringUtils.join(args));
        }

        // 服务的远程调用点
        final CallPoint callPoint = getServiceCallPoint();
        // 当前线程的调用点
        final FromPoint fromPoint = ServicePort.getFromPoint();

        // rpc调用
        final RpcInvocation rpcInvocation = new RpcInvocation(fromPoint, callPoint, methodName, args);
        rpcInvocation.invoke();

        return null;
    }

    /**
     * 通过Service接口类的 {@link ServiceConfig} 的注解获取 {@link CallPoint}
     * @return 返回调用点
     */
    private CallPoint getServiceCallPoint() {
        final ServiceConfig serviceConfig = type.getAnnotation(ServiceConfig.class);
        final String serviceName = type.getName();
        return new CallPoint(serviceConfig.node(), serviceConfig.port(), serviceName);
    }

}