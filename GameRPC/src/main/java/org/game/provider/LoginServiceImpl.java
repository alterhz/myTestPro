package org.game.provider;

import org.game.core.ServiceBase;
import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.game.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class LoginServiceImpl extends ServiceBase implements LoginService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private int loginId = 100;

    @Override public CompletableFuture<Integer> login() {
        logger.trace("LoginServiceImpl.login");

        logger.info("demoService.go(\"tom\")");
        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        demoService.go("tom");
        return CompletableFuture.completedFuture(20);
    }

    @Override
    public Integer allocLoginId() {
        return loginId++;
    }

    @Override public void init() {
        logger.trace("LoginServiceImpl.init");
    }
}
