package org.game.provider;

import org.game.core.ServiceBase;
import org.game.core.refer.ReferenceFactory;
import org.game.service.DemoService;
import org.game.service.InitService;
import org.game.service.LoginService;

public class InitServiceImpl extends ServiceBase implements InitService {

    @Override
    public void init() {
        final DemoService demoService = ReferenceFactory.getProxy(DemoService.class);
        demoService.test();

        final LoginService loginService = ReferenceFactory.getProxy(LoginService.class);
        loginService.login();
    }
}
