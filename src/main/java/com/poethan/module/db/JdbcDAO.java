package com.poethan.gear.module.db;

import com.poethan.gear.utils.EzDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class JdbcDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public <D extends BaseDO<ID>, ID> List<D> findByIds(Class<D> tClass, List<ID> ids) {
        Table annotation = tClass.getAnnotation(Table.class);
        if (Objects.isNull(annotation)) {
            return null;
        }
        String table = annotation.name();
        return jdbcTemplate.queryForList("select * from " + table + " where id in (?)", tClass, ids);
    }

    public <D extends BaseDO<ID>, ID> List<D> findByIdsNew(Class<D> tClass, List<ID> ids) {
        Table annotation = tClass.getAnnotation(Table.class);
        if (Objects.isNull(annotation)) {
            return null;
        }
        String table = annotation.name();
        return jdbcTemplate.query("select * from " + table + " where id in (?)", ids.toArray(), new NamedPropertyRowMapper<>(tClass));
    }

    public <D extends BaseDO<ID>, ID> D findById(Class<D> tClass, ID id) {
        Table annotation = tClass.getAnnotation(Table.class);
        if (Objects.isNull(annotation)) {
            return null;
        }
        String table = annotation.name();

        return jdbcTemplate.queryForObject("select * from " + table + " where id = ? ", (rs, rowNum) -> {
            try {
                D domain = tClass.newInstance();
                domain.setId(id);
                Object createTime = rs.getObject("create_time");
                if (createTime instanceof String) {
                    domain.setCreateTime(new EzDate((String) createTime));
                } else if (createTime instanceof Long) {
                    domain.setCreateTime(new EzDate((Long) createTime));
                } else if (createTime instanceof LocalDateTime) {
                    domain.setCreateTime(new EzDate((LocalDateTime) createTime));
                }
                Object updateTime = rs.getObject("update_time");
                if (updateTime instanceof String) {
                    domain.setUpdateTime(new EzDate((String) updateTime));
                } else if (updateTime instanceof Long) {
                    domain.setUpdateTime(new EzDate((Long) updateTime));
                } else if (updateTime instanceof LocalDateTime) {
                    domain.setUpdateTime(new EzDate((LocalDateTime) updateTime));
                }
                Field[] fields = domain.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    field.setAccessible(true);
                    field.set(domain, rs.getObject(column.name()));
                    field.setAccessible(false);
                }
                return domain;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, id);
    }

    public <D extends BaseDO<ID>, ID> D findByIdNew(Class<D> tClass, ID id) {
        String table = tClass.getAnnotation(Table.class).name();
        return jdbcTemplate.queryForObject("select * from " + table + " where id = ? ", new NamedPropertyRowMapper<>(tClass), id);
    }
}
