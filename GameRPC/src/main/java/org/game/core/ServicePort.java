package org.game.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServicePort implements Runnable {

    /**
     * 当前Service线程的
     */
    private static final ThreadLocal<FromPoint> CALL_POINT_THREAD_LOCAL = new ThreadLocal<>();

    private final String name;
    /** 服务列表 {@literal name -> Service} */
    private final Map<String, Service> services = new ConcurrentHashMap<>();
    private final ServiceNode parentNode;

    public ServicePort(String name, ServiceNode parentNode) {
        this.name = name;
        this.parentNode = parentNode;
    }

    public void init() {

    }

    /**
     * 获取当前线程的调用点，{@link CallPoint#getService()} {@literal ==""}
     */
    public static FromPoint getFromPoint() {
        return CALL_POINT_THREAD_LOCAL.get();
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
        CALL_POINT_THREAD_LOCAL.set(new FromPoint(parentNode.getName(), getName()));



        CALL_POINT_THREAD_LOCAL.set(null);
    }
}
