package org.game.core.exchange;

import org.game.core.RpcInvocation;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * rpc请求对象
 * @author Ziegler
 * date 2021/3/15
 */
public class Request implements Serializable {

    private static final AtomicLong INVOKE_ID = new AtomicLong();

    /** 请求id */
    private Long id;

    /** rpc调用数据 */
    private RpcInvocation rpcInvocation;

    public Request() {
        this.id = 0L;
    }

    public Request(long id) {
        this.id = id;
    }

    public static long allocId() {
        return INVOKE_ID.getAndIncrement();
    }

    public Long getId() {
        return id;
    }

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }
}
