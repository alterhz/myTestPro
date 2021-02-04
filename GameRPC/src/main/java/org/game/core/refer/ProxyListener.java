package org.game.core.refer;

import org.jow.core.support.Param;
import org.jow.core.support.function.JowFunction2;

public interface ProxyListener {

    void listenResult(JowFunction2<Param, Param> method, Param context);

}
