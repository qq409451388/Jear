package com.poethan.jear.jdbc;


import com.poethan.jear.core.utils.EzDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class FieldRowMapper<T> implements RowMapper<T> {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private Class<T> mappedClass;

    public FieldRowMapper(Class<T> clazz) {
        this.mappedClass = clazz;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        if (0 == rowNum) {
            return null;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        if (0 == rsmd.getColumnCount()) {
            return null;
        } else if (1 != rsmd.getColumnCount()) {
            throw new IncorrectResultSetColumnCountException(1, rsmd.getColumnCount());
        }
        if (mappedClass.isAssignableFrom(String.class)) {
            return (T) rs.getString(1);
        } else if (mappedClass.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(rs.getLong(1));
        } else if (mappedClass.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(rs.getInt(1));
        } else if (mappedClass.isAssignableFrom(BigDecimal.class)) {
            return (T) rs.getBigDecimal(1);
        } else if (mappedClass.isAssignableFrom(EzDate.class)) {
            Timestamp timestamp = rs.getTimestamp(1);
            if (Objects.isNull(timestamp)) {
                return (T) EzDate.ZERO_TIME;
            } else {
                return (T) new EzDate(timestamp.getTime());
            }
        } else if (mappedClass.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(rs.getFloat(1));
        } else if (mappedClass.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(rs.getDouble(1));
        } else if (mappedClass.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(rs.getBoolean(1));
        } else {
            return (T) rs.getObject(1);
        }
    }
}
