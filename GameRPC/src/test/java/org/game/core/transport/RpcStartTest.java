package org.game.core.transport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RpcStartTest {
    public static void main(String[] args) {
        new RpcServer().start();
        new RpcClient().start();
    }
}