package org.game.provider;

import org.game.core.ServiceBase;
import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class DemoServiceImpl extends ServiceBase implements DemoService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public void test() {
        logger.trace("DemoServiceImpl.test");

        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        final CompletableFuture<String> future = demoService.getServiceName();
        future.whenComplete((s, throwable) -> {
            //
            logger.trace("获取Service名称：" + s);
        });
    }

    @Override
    public void go(String name) {
        logger.trace("DemoServiceImpl.go name = {}", name);
    }

    @Override
    public CompletableFuture<String> getServiceName() {
        logger.trace("DemoServiceImpl.getServiceName");
        return CompletableFuture.completedFuture("DemoServiceImpl");
    }

    @Override
    public void init() {
        logger.trace("DemoServiceImpl.init");
    }
}
