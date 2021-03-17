package org.game.core;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.game.core.exchange.Request;
import org.game.core.exchange.Response;
import org.game.core.exchange.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServicePort implements Runnable {

    /** 所属node */
    private static final ThreadLocal<ServicePort> THREAD_LOCAL_SERVICE_PORT = new ThreadLocal<>();

    private final String name;
    /** 服务列表 {@literal name -> Service} */
    private final Map<String, Service> services = new ConcurrentHashMap<>();
    private final ServiceNode parentNode;
    /** rpc发送地址 */
    private final FromPoint fromPoint;

    /** 收到的{@link Request}线程安全队列  */
    private final ConcurrentLinkedQueue<Request> receivedRequests = new ConcurrentLinkedQueue<>();

    /** 收到的{@link Response}线程安全队列 */
    private final ConcurrentLinkedQueue<Response> receivedResponses = new ConcurrentLinkedQueue<>();

    /** future的超时处理 */
    private final Map<Long, DefaultFuture> allFutureTimeout = new ConcurrentHashMap<>();

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ServicePort.class);

    public ServicePort(String name, ServiceNode parentNode) {
        this.name = name;
        this.parentNode = parentNode;
        this.fromPoint = new FromPoint(parentNode.getName(), getName());
    }

    /**
     * 添加{@link DefaultFuture}超时处理
     */
    public void addFutureTimeoutTask(DefaultFuture future) {
        allFutureTimeout.put(future.getId(), future);
        logger.debug("add future. future = {}, threadId = {}", future, Thread.currentThread().getId());
    }

    public void init() {

    }

    public void addRequest(Request request) {
        receivedRequests.add(request);
    }

    public void addResponse(Response response) {
        receivedResponses.add(response);
    }

    /** 线程所属node */
    public static ServicePort getServicePort() {
        return THREAD_LOCAL_SERVICE_PORT.get();
    }

    /**
     * RPC调用地址
     */
    public FromPoint getFromPoint() {
        return new FromPoint(parentNode.getName(), getName());
    }

    public void addService(String name, Service service) {
        services.put(name, service);
    }

    public ServiceNode getServiceNode() {
        return parentNode;
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
        THREAD_LOCAL_SERVICE_PORT.set(this);

        for (Service service : services.values()) {
            service.init();
        }

        while (!Thread.currentThread().isInterrupted()) {
            pollRequest();
            pollResponse();

            futureTimeout();
        }

        THREAD_LOCAL_SERVICE_PORT.set(null);
    }

    private void futureTimeout() {
        allFutureTimeout.entrySet().removeIf(entry -> {
            final DefaultFuture defaultFuture = entry.getValue();
            final boolean timeout = defaultFuture.isTimeout();
            if (timeout) {
                defaultFuture.cancel(true);
                logger.warn("DefaultFuture超时。defaultFuture = {}", defaultFuture.toString());
            }
            return timeout;
        });
    }

    private void pollResponse() {
        final Response response = receivedResponses.poll();
        if (response == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("execute response = {}", response);
            final DefaultFuture future = DefaultFuture.getFuture(response.getId());
            if (response.getStatus() == 0) {
                future.complete(response.getResult());
            } else {
                future.completeExceptionally(new RuntimeException("出现错误!"));
            }
            final DefaultFuture removeFuture = allFutureTimeout.remove(response.getId());
            logger.debug("remove future. future = {}, threadId = {}", removeFuture, Thread.currentThread().getId());
        }
    }

    private void pollRequest() {
        final Request request = receivedRequests.poll();
        if (request == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("request == null. e = {}", e.getMessage());
            }
        } else {
            RpcInvocation rpcInvocation = request.getRpcInvocation();
            final String serviceName = rpcInvocation.getCallPoint().getService();
            final String methodName = rpcInvocation.getMethodName();
            final Service service = services.get(serviceName);
            if (service == null) {
                logger.warn("service == null.serviceName = {}", serviceName);
            } else {
                try {
                    final Object result = MethodUtils.invokeExactMethod(service, methodName, rpcInvocation.getMethodArgs());
                    if (result == null) {
                        logger.debug("RPC return type : void");
                    } else {
                        if (result instanceof CompletableFuture) {
                            // 返回应答消息
                            final CompletableFuture<?> completableFuture = (CompletableFuture<?>) result;
                            completableFuture.whenComplete((o, throwable) -> {
                                final Response response = new Response(request.getId(), 0);
                                response.setResult(o);

                                try {
                                    final byte[] buffer = Utils.encode(response);
                                    Response decodeResponse = Utils.decode(buffer);
                                    // 返回应答消息
                                    final ServiceNode serviceNode = ServicePort.getServicePort().getServiceNode();
                                    serviceNode.dispatchResponse(decodeResponse);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    logger.error("转发Response异常：{}", e.getMessage());
                                }
                            });
                        }
                    }
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
}
