package com.poethan.jear.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EzRpcResponse<T> extends BaseVO {
    private int code;
    private T data;
    private String msg;

    private EzRpcResponse () {
        this.code = 0;
        this.msg = "";
    }

    public static <T> EzRpcResponse<T> OK (T data) {
        EzRpcResponse<T> o = new EzRpcResponse<>();
        o.data = data;
        return o;
    }

    public static <T> EzRpcResponse<T> ERROR (int code, String msg) {
        EzRpcResponse<T> o = new EzRpcResponse<>();
        o.code = code;
        o.msg = msg;
        return o;
    }
    public static <T> EzRpcResponse<T> ERROR (int code) {
        return EzRpcResponse.ERROR(code, "ERROR");
    }
}
