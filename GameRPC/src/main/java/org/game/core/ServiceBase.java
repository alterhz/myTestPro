package org.game.core;

import java.util.concurrent.TimeUnit;

public abstract class ServiceBase implements Service {

    private long nextSecondPulse;

    @Override
    public void pulse(long now) {
        if (now > nextSecondPulse) {
            nextSecondPulse += TimeUnit.SECONDS.toMillis(1L);
            pulseEverySecond(now);
        }
    }

    /**
     * 每秒一次的心跳
     * @param now 时间戳
     */
    protected void pulseEverySecond(long now) {

    }
}
