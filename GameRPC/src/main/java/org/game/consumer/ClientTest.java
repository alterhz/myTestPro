package org.game.consumer;

import java.util.concurrent.CompletableFuture;

import org.game.service.DemoService;
import org.game.core.refer.ReferenceFactory;

public class ClientTest {

    public static void main(String[] args) {
        final ClientTest clientTest = new ClientTest();
        clientTest.callDemoService();
    }

    void callDemoService() {
        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        demoService.test();
        demoService.go("jack");
        final CompletableFuture<String> serviceName = demoService.getServiceName();
        serviceName.whenComplete((name, throwable) -> System.out.println("name = " + name));
    }
}
