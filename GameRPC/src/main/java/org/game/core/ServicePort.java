package org.game.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicePort implements Runnable {

    /**
     * 当前Service线程的
     */
    private static final ThreadLocal<FromPoint> THREAD_LOCAL_FROM_POINT = new ThreadLocal<>();

    /** 所属node */
    private static final ThreadLocal<ServiceNode> THREAD_LOCAL_SERVICE_NODE = new ThreadLocal<>();

    private final String name;
    /** 服务列表 {@literal name -> Service} */
    private final Map<String, Service> services = new ConcurrentHashMap<>();
    private final ServiceNode parentNode;

    private final ConcurrentLinkedQueue<RpcInvocation> receivedRpcInvocations = new ConcurrentLinkedQueue<>();

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ServicePort.class);

    public ServicePort(String name, ServiceNode parentNode) {
        this.name = name;
        this.parentNode = parentNode;
    }

    public void init() {

    }

    public void addRpcInvocation(RpcInvocation rpcInvocation) {
        receivedRpcInvocations.add(rpcInvocation);
    }

    /**
     * 获取当前线程的调用点，{@link CallPoint#getService()} {@literal ==""}
     */
    public static FromPoint getFromPoint() {
        return THREAD_LOCAL_FROM_POINT.get();
    }

    /** 线程所属node */
    public static ServiceNode getServiceNode() {
        return THREAD_LOCAL_SERVICE_NODE.get();
    }

    public void addService(String name, Service service) {
        services.put(name, service);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .toString();
    }

    @Override
    public void run() {
        THREAD_LOCAL_FROM_POINT.set(new FromPoint(parentNode.getName(), getName()));
        THREAD_LOCAL_SERVICE_NODE.set(parentNode);

        for (Service service : services.values()) {
            service.init();
        }

        while (!Thread.currentThread().isInterrupted()) {
            final RpcInvocation rpcInvocation = receivedRpcInvocations.poll();
            if (rpcInvocation == null) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                final String serviceName = rpcInvocation.getCallPoint().getService();
                final String methodName = rpcInvocation.getMethodName();
                final Service service = services.get(serviceName);
                if (service == null) {
                    logger.warn("service == null.serviceName = {}", serviceName);
                } else {
                    try {
                        MethodUtils.invokeExactMethod(service, methodName, rpcInvocation.getMethodArgs());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        THREAD_LOCAL_FROM_POINT.set(null);
        THREAD_LOCAL_SERVICE_NODE.set(null);
    }
}
