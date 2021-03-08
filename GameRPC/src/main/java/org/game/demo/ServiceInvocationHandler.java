package org.game.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceInvocationHandler implements InvocationHandler {
    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method = " + method.getName());
        if ("getName".equals(method.getName())) {
            return "demoService";
        }
        return null;
    }
}
