package org.game.global;

import org.game.core.ServiceBase;
import org.game.core.ServiceConfig;
import org.game.core.ServiceNode;
import org.game.core.ServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ServiceUtils {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

    /**
     * 通过服务接口类添加服务到对应node上的port上
     * @param serviceNode 指定的node
     * @param serviceInterface 服务接口定义
     * @throws IllegalAccessException 添加不在当前node节点的service就会有此异常
     * @throws NoSuchMethodException 获取ServiceImpl的构造函数失败
     * @throws InvocationTargetException 创建ServiceImpl实例
     * @throws InstantiationException 创建ServiceImpl实例
     */
    public static void addService(ServiceNode serviceNode, Class<?> serviceInterface) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String serviceName = serviceInterface.getName();
        final ServiceConfig serviceConfig = serviceInterface.getAnnotation(ServiceConfig.class);
        if (!serviceConfig.node().equals(serviceNode.getName())) {
            throw new IllegalStateException("当前服务不在当前node启动。serviceName = " + serviceName + ", node = " + serviceConfig
                    .node() + ", current Node = " + serviceNode.getName());
        }
        final Class<? extends ServiceBase> serviceImplType = serviceConfig.serviceImplType();
        final Constructor<? extends ServiceBase> constructor = serviceImplType.getConstructor();
        final ServiceBase serviceBase = constructor.newInstance();
        final String portName = serviceConfig.port();
        final ServicePort servicePort = serviceNode.getServicePort(portName);
        if (servicePort == null) {
            serviceNode.addServicePort(new ServicePort(portName, serviceNode));
            logger.info("addServicePort portName = {}", portName);
        }
        serviceNode.getServicePort(portName).addService(serviceName, serviceBase);
    }

}
