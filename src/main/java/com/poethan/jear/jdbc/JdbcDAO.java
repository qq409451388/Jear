package com.poethan.jear.jdbc;

import com.alibaba.druid.util.JdbcUtils;
import com.poethan.utils.EzDataUtils;
import com.poethan.jear.utils.EzDate;
import com.poethan.jear.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JdbcDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public <D extends BaseDO<ID>, ID> List<D> findByIds(Class<D> tClass, List<ID> ids) {
        Table annotation = tClass.getAnnotation(Table.class);
        String table = annotation.name();
        return jdbcTemplate.queryForList("select * from " + table + " where id in (?)", tClass, ids);
    }

    public <D extends BaseDO<ID>, ID> List<D> findByIdsNew(Class<D> tClass, List<ID> ids) {
        return this.findListByCondition(tClass, "where id in ( :ids )", SqlParam.create("ids", ids));
    }

    public <D extends BaseDO<ID>, ID> D findById(Class<D> tClass, ID id) {
        Table annotation = tClass.getAnnotation(Table.class);
        String table = annotation.name();

        return jdbcTemplate.queryForObject("select * from " + table + " where id = ? ", (rs, rowNum) -> {
            try {
                D domain = tClass.newInstance();
                domain.setId(id);
                domain.setCreateTime(new EzDate(rs.getInt("create_time")));
                domain.setUpdateTime(new EzDate(rs.getInt("update_time")));
                Field[] fields = domain.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    field.setAccessible(true);
                    field.set(domain, rs.getObject(column.name()));
                    field.setAccessible(false);
                }
                return domain;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, id);
    }

    public <D extends BaseDO<ID>, ID> D findByIdNew(Class<D> tClass, ID id) {
        return this.findByCondition(tClass, "where id = :id", SqlParam.create("id", id));
    }

    public <D extends BaseDO<ID>, ID> D findByCondition(Class<D> tClass, String whereSql, SqlParam sqlParam) {
        try {
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return null;
            }
            whereSql = whereSql + " ";
            Map<String, Object> map = sqlParam.getData();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                whereSql = whereSql.replaceAll(":" + entry.getKey() + " ", entry.getValue().toString() + " ");
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            return jdbcTemplate.queryForObject("select * from " + tableName + " " + whereSql,
                    new NamedPropertyRowMapper<>(tClass));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <D extends BaseDO<ID>, ID> List<D> findListByCondition(Class<D> tClass, String whereSql, SqlParam sqlParam) {
        try {
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return null;
            }
            whereSql = whereSql + " ";
            Map<String, Object> map = sqlParam.getData();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (EzDataUtils.isCollectionNoKey(entry.getValue())) {
                    String value = ((Collection<Object>)entry.getValue()).stream().map(String::valueOf).collect(Collectors.joining(","));
                    whereSql = whereSql.replaceAll(":" + entry.getKey() + " ", value + " ");
                } else if (EzDataUtils.isScalar(entry.getValue())) {
                    whereSql = whereSql.replaceAll(":" + entry.getKey() + " ", entry.getValue() + " ");
                } else {
                    throw new EmptyResultDataAccessException(0);
                }
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            return jdbcTemplate.query("select * from " + tableName + " " + whereSql,
                    new NamedPropertyRowMapper<>(tClass));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
