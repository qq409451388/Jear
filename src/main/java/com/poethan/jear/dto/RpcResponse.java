package com.poethan.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RpcResponse<T> extends BaseDTO {
    private Integer code;
    private String msg;
    private T data;

    public RpcResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> RpcResponse<T> OK(T data) {
        return new RpcResponse<>(0, "", data);
    }

}
