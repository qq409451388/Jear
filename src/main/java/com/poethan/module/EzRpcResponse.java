package com.poethan.gear.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

@Getter
@Setter
@ToString
public class EzRpcResponse extends BaseVO {
    private int code;
    private Object data;
    private String msg;

    private EzRpcResponse () {
        this.code = 0;
        this.msg = Strings.EMPTY;
    }

    public static EzRpcResponse OK (Object data) {
        EzRpcResponse o = new EzRpcResponse();
        o.data = data;
        return o;
    }
}
