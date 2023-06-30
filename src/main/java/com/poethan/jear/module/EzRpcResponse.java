package com.poethan.jear.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EzRpcResponse extends BaseVO {
    private int code;
    private Object data;
    private String msg;

    private EzRpcResponse () {
        this.code = 0;
        this.msg = "";
    }

    public static EzRpcResponse OK (Object data) {
        EzRpcResponse o = new EzRpcResponse();
        o.data = data;
        return o;
    }

    public static EzRpcResponse ERROR (int code, String msg) {
        EzRpcResponse o = new EzRpcResponse();
        o.code = code;
        o.msg = msg;
        return o;
    }
    public static EzRpcResponse ERROR (int code) {
        return EzRpcResponse.ERROR(code, "ERROR");
    }
}
