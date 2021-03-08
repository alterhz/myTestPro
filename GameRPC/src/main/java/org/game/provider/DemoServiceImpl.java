package org.game.provider;

import java.util.concurrent.CompletableFuture;

import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.game.core.ServiceBase;
import org.game.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoServiceImpl extends ServiceBase implements DemoService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public void test() {
        logger.info("DemoServiceImpl.test");

        final LoginService loginService = ReferenceFactory.getProxy(LoginService.class);
        loginService.login();
    }

    @Override
    public void go() {
        logger.info("DemoServiceImpl.go");
    }

    @Override
    public CompletableFuture<String> getServiceName() {
        return CompletableFuture.completedFuture("DemoServiceImpl");
    }

    @Override
    public void init() {
        logger.info("DemoServiceImpl.init");
    }
}
