package org.game.service;

import java.util.concurrent.CompletableFuture;

import org.game.core.ServiceConfig;
import org.game.provider.DemoServiceImpl;

@ServiceConfig(node = ServiceConsts.NODE0,
        port = ServiceConsts.PORT0,
        serviceImplType = DemoServiceImpl.class)
public interface DemoService {
    void test();

    void go();

    CompletableFuture<String> getServiceName();
}
