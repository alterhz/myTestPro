package org.game.service;

import java.util.concurrent.CompletableFuture;

import org.game.global.ServiceConsts;
import org.game.core.ServiceConfig;
import org.game.provider.DemoServiceImpl;

@ServiceConfig(node = ServiceConsts.NODE0,
        port = ServiceConsts.PORT1,
        serviceImplType = DemoServiceImpl.class)
public interface DemoService {
    void test();

    void go(String name);

    CompletableFuture<String> getServiceName();
}
