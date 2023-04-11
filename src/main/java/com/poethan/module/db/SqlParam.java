package com.poethan.gear.module.db;

import java.util.HashMap;
import java.util.Map;

public class SqlParam {
    private final Map<String, Object> map = new HashMap<>();

    public static SqlParam create(String k, Object v) {
        SqlParam sqlParam = new SqlParam();
        sqlParam.add(k, v);
        return sqlParam;
    }

    public SqlParam add(String k, Object v) {
        map.put(k, v);
        return this;
    }
}
