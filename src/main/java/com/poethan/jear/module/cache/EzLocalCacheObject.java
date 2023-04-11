package com.poethan.jear.module.cache;

import com.poethan.jear.module.BaseVO;
import com.poethan.jear.utils.SystemUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EzLocalCacheObject extends BaseVO {
    private Object dataSource;
    private int dataType;
    private int expire = 0;

    public final static int T_STRING = 1;
    public final static int T_INTEGER = 2;
    public final static int T_FLOAT = 3;
    public final static int T_LIST = 4;
    public final static int T_MAP = 5;
    public final static int T_OBJECT = 10;

    public static EzLocalCacheObject create (Object value, int expire, int dataType) {
        EzLocalCacheObject o = new EzLocalCacheObject();
        o.setDataSource(value);
        o.setExpire(expire);
        o.setDataType(dataType);
        return o;
    }

    public static EzLocalCacheObject create (Object value) {
        return create(value, 0, T_STRING);
    }

    public boolean isList() {
        return T_LIST == this.dataType;
    }

    public boolean isMap() {
        return T_MAP == this.dataType;
    }

    public boolean isNormal() {
        return !this.isList() && !this.isMap();
    }

    public boolean isString() {
        return T_STRING == this.dataType;
    }

    public boolean isExpire() {
        return this.expire > SystemUtils.currentTimeStamp();
    }
}
