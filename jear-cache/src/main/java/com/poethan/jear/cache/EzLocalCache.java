package com.poethan.jear.cache;


import com.poethan.jear.core.utils.SystemUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EzLocalCache extends EzCache {
    private static final ConcurrentHashMap<String, EzLocalCacheObject> data = new ConcurrentHashMap<>();
    private static final String EXCEPTION_PREFIX = "[EzLocalCache Exception] ";
    private static final String UNSUPPORT_COMMAND = EXCEPTION_PREFIX + "Unsupport Command %s From %s";

    private boolean has(String key) {
        return Objects.nonNull(data.get(key));
    }

    private EzLocalCacheObject fetch(String key) {
        return data.get(key);
    }

    private void unsupportException(String key, int operateDataType, String funcName) {
        EzLocalCacheObject object = this.fetch(key);
        switch (operateDataType) {
            case EzLocalCacheObject.T_LIST:
                //DBC.assertTrue(object.isList(), String.format(UNSUPPORT_COMMAND, funcName, object.getDataType()));
                break;
            case EzLocalCacheObject.T_MAP:
                //DBC.assertTrue(object.isMap(), String.format(UNSUPPORT_COMMAND, funcName, object.getDataType()));
                break;
            case EzLocalCacheObject.T_STRING:
            case EzLocalCacheObject.T_INTEGER:
            case EzLocalCacheObject.T_FLOAT:
            default:
                //DBC.assertTrue(object.isNormal(), String.format(UNSUPPORT_COMMAND, funcName, object.getDataType()));
                break;
        }
    }

    @Deprecated
    public Map<String, EzLocalCacheObject> getAll() {
        return data;
    }

    private boolean isExpire(String key) {
        if (!this.has(key)) {
            return true;
        }
        return this.fetch(key).isExpire();
    }

    public boolean exists(String key) {
        if (!this.has(key) || (this.isExpire(key) && this.del(key)) ) {
            return false;
        }
        return true;
    }

    public boolean del (String key) {
        data.remove(key);
        return true;
    }

    public boolean expire (String key, int expire) {
        //DBC.assertNonNull(this.fetch(key), EXCEPTION_PREFIX + "Expire No Match Key "+ key);
        this.fetch(key).setExpire(SystemUtils.currentTimeStamp() + expire);
        return true;
    }

    public boolean set (String key, String value) {
        data.put(key, EzLocalCacheObject.create(value));
        return true;
    }

    public boolean setSourceEX (String key, Object value, int expire) {
        data.put(key, EzLocalCacheObject.create(value, expire, EzLocalCacheObject.T_OBJECT));
        return true;
    }

    public Object getSource (String key) {
        if (!this.exists(key)) {
            return null;
        }
        return this.fetch(key).getDataSource();
    }

    public boolean setEX(String key, String value, int expire) {
        data.put(key, EzLocalCacheObject.create(value, expire, EzLocalCacheObject.T_STRING));
        return true;
    }

    public String get(String key) {
        if (!this.exists(key)) {
            return "";
        }
        if (!this.fetch(key).isString()) {
            return "";
        }
        return (String) this.fetch(key).getDataSource();
    }

    public boolean flushAll () {
        data.clear();
        return true;
    }
}
