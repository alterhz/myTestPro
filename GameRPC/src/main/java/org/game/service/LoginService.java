package org.game.service;

import org.game.core.ServiceConfig;
import org.game.provider.LoginServiceImpl;

@ServiceConfig(node = ServiceConsts.NODE0,
        port = ServiceConsts.PORT0,
        serviceImplType = LoginServiceImpl.class)
public interface LoginService {
    void login();
}
