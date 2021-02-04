package org.game.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServiceNode {

    private final String name;
    /** 服务器线程列表 */
    private final Map<String, ServicePort> servicePorts = new HashMap<>();

    private final ExecutorService executorService;

    public ServiceNode(String name, ExecutorService executorService) {
        this.name = name;
        this.executorService = executorService;
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
