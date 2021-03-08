package org.game.demo;

import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        final DemoService demoService = (DemoService) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{DemoService.class}, new ServiceInvocationHandler());
        final String name = demoService.getName();
        System.out.println("name = " + name);
    }

}
