package org.game;

import java.util.concurrent.Executors;

import org.game.core.ServiceNode;
import org.game.core.ServicePort;
import org.game.core.refer.ReferenceFactory;
import org.game.provider.DemoServiceImpl;
import org.game.provider.LoginServiceImpl;
import org.game.service.DemoService;
import org.game.service.LoginService;

public class StartUp {

    public static void main(String[] args) {
        final StartUp startUp = new StartUp();
        startUp.run(args);
    }

    /**
     * 启动参数 node0 port0
     */
    void run(String args[]) {

        final ServiceNode serviceNode = new ServiceNode("node0", Executors.newCachedThreadPool());
        serviceNode.addServicePort(new ServicePort("port0", serviceNode));
        serviceNode.addServicePort(new ServicePort("port1", serviceNode));
        serviceNode.addServicePort(new ServicePort("port2", serviceNode));
        serviceNode.addServicePort(new ServicePort("port3", serviceNode));

        serviceNode.getServicePort("port0").addService(DemoService.class.getName(),
                                                       new DemoServiceImpl());
        serviceNode.getServicePort("port0").addService(LoginService.class.getName(),
                                                       new LoginServiceImpl());

        serviceNode.startAllService();


//        final Reflections reflections = new Reflections("org.game.service");
//        final Set<Class<?>> serviceTypes = reflections.getTypesAnnotatedWith(
//                ServiceConfig.class);
//        for (Class<?> serviceType : serviceTypes) {
//            System.out.println("serviceType = " + serviceType.getName());
//        }

    }
}
