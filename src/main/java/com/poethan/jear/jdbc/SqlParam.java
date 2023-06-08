package com.poethan.jear.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SqlParam {
    private Map<String, Object> data;

    public static SqlParam create() {
        return new SqlParam();
    }

    public static SqlParam create(String k, Object v) {
        SqlParam sqlParam = create();
        sqlParam.data = new HashMap<>(16);
        sqlParam.add(k, v);
        return sqlParam;
    }

    public SqlParam add(String k, Object v) {
        this.data.put(k, v);
        return this;
    }

    public Object[] toArray() {
        Object[] objects = new Object[data.size()*2];
        int index = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            objects[index++] = entry.getValue();
        }
        return objects;
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        this.data.forEach(action);
    }

    public Map<String, Object> getData() {
        return this.data;
    }
}
