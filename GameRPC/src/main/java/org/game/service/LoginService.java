package org.game.service;

import org.game.global.ServiceConsts;
import org.game.core.ServiceConfig;
import org.game.provider.LoginServiceImpl;

@ServiceConfig(node = ServiceConsts.NODE0,
        port = ServiceConsts.PORT2,
        serviceImplType = LoginServiceImpl.class)
public interface LoginService {
    void login();
}
