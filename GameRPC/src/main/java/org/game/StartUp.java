package org.game;

import org.game.core.ServiceNode;
import org.game.core.ServicePort;
import org.game.global.ServiceConsts;
import org.game.global.ServiceUtils;
import org.game.provider.DemoServiceImpl;
import org.game.provider.InitServiceImpl;
import org.game.provider.LoginServiceImpl;
import org.game.service.DemoService;
import org.game.service.InitService;
import org.game.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executors;

public class StartUp {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(StartUp.class);

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final StartUp startUp = new StartUp();
        startUp.run(args);
    }

    /**
     * 启动参数 node0 port0
     */
    void run(String args[]) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (logger.isTraceEnabled()) {
            logger.info("StartUp");
        }

        final ServiceNode serviceNode = new ServiceNode(ServiceConsts.NODE0, Executors.newCachedThreadPool());
        serviceNode.addServicePort(new ServicePort(ServiceConsts.PORT0, serviceNode));
        serviceNode.addServicePort(new ServicePort(ServiceConsts.PORT1, serviceNode));
        serviceNode.addServicePort(new ServicePort(ServiceConsts.PORT2, serviceNode));

        if (true) {
            ServiceUtils.addService(serviceNode, InitService.class);
            ServiceUtils.addService(serviceNode, DemoService.class);
            ServiceUtils.addService(serviceNode, LoginService.class);
        } else {
            serviceNode.getServicePort(ServiceConsts.PORT0).addService(InitService.class.getName(),
                                                           new InitServiceImpl());
            serviceNode.getServicePort(ServiceConsts.PORT1).addService(DemoService.class.getName(),
                                                           new DemoServiceImpl());
            serviceNode.getServicePort(ServiceConsts.PORT2).addService(LoginService.class.getName(),
                                                           new LoginServiceImpl());
        }

        serviceNode.startAllService();


//        final Reflections reflections = new Reflections("org.game.service");
//        final Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(
//                ServiceConfig.class);
//        for (Class<?> serviceType : serviceTypes) {
//            System.out.println("serviceType = " + serviceType.getName());
//        }

    }
}
