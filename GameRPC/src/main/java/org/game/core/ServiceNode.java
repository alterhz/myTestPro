package org.game.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.game.core.exchange.Request;
import org.game.core.exchange.Response;
import org.game.core.exchange.Utils;

public class ServiceNode {

    private final String name;
    /** 服务器线程列表 */
    private final Map<String, ServicePort> servicePorts = new ConcurrentHashMap<>();

    private final ExecutorService executorService;

    public ServiceNode(String name, ExecutorService executorService) {
        this.name = name;
        this.executorService = executorService;
    }

    /**
     * 派发{@link Request}到对应的{@link ServicePort}
     * @param request rpc请求对象
     */
    public void dispatchRequest(Request request) {
        final RpcInvocation rpcInvocation = request.getRpcInvocation();
        final String portName = rpcInvocation.getCallPoint().getPort();
        final ServicePort servicePort = servicePorts.get(portName);
        servicePort.addRequest(request);
    }

    /**
     * 派发{@link Response}应答对象
     * @param response 应答对象
     */
    public void dispatchResponse(Response response) {
        final DefaultFuture future = DefaultFuture.getFuture(response.getId());
        final RpcInvocation rpcInvocation = future.getRequest().getRpcInvocation();
        final String portName = rpcInvocation.getCallPoint().getPort();
        final ServicePort servicePort = servicePorts.get(portName);
        servicePort.addResponse(response);
    }

    public void addServicePort(ServicePort servicePort) {
        if (servicePorts.containsKey(servicePort.getName())) {
            return;
        }
        servicePort.init();
        servicePorts.put(servicePort.getName(), servicePort);
    }

    /**
     * 通过服务Port名称获取
     * @param portName port名称
     * @return 没有匹配到返回 {@code null}
     */
    public ServicePort getServicePort(String portName) {
        return servicePorts.get(portName);
    }

    public void startAllService() {
        for (ServicePort servicePort : servicePorts.values()) {
            executorService.execute(servicePort);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .toString();
    }
}
