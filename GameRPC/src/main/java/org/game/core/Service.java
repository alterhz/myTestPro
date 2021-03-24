package org.game.core;

/**
 * 服务基类
 *
 * @author Ziegler
 * date 2021/3/8
 */
public interface Service {
    void init();

    void pulse(long now);
}
