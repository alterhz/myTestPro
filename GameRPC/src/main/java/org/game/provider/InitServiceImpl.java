package org.game.provider;

import org.game.core.ServiceBase;
import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.game.service.InitService;
import org.game.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class InitServiceImpl extends ServiceBase implements InitService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);

    @Override
    public void init() {
        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        demoService.test();

        final LoginService loginService = ReferenceFactory.getProxy(LoginService.class);
        final CompletableFuture<Integer> loginResult = loginService.login();
        loginResult.whenComplete((value, throwable) -> logger.info("login 返回结果：= {}", value));
    }
}
