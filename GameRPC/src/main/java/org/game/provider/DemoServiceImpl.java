package org.game.provider;

import java.util.concurrent.CompletableFuture;

import org.game.service.DemoService;
import org.game.core.ServiceBase;

public class DemoServiceImpl extends ServiceBase implements DemoService {

    @Override
    public void test() {
        System.out.println("DemoServiceImpl.test");
    }

    @Override public void go() {

    }

    @Override
    public CompletableFuture<String> getServiceName() {
        return CompletableFuture.completedFuture("DemoServiceImpl");
    }
}
