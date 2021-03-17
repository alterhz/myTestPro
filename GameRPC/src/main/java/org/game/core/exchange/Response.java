package org.game.core.exchange;

import java.io.Serializable;

public class Response implements Serializable {

    private Long id;
    private int status = 0;
    private Object result;

    public Response() {
    }

    public Response(Long id, int status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
