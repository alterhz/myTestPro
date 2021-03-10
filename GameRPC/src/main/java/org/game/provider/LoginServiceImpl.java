package org.game.provider;

import org.game.core.ServiceBase;
import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.game.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServiceImpl extends ServiceBase implements LoginService {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override public void login() {
        logger.info("LoginServiceImpl.login");
        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        demoService.go("tom");
    }

    @Override public void init() {
        logger.info("LoginServiceImpl.init");
    }
}
