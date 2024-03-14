package com.poethan.jear.jdbc;

import com.poethan.jear.core.utils.EzDataUtils;
import com.poethan.jear.core.utils.EzDate;
import com.poethan.jear.core.utils.EzString;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class JdbcDAO {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private boolean isUseNew = true;

    public <D extends AbstractDO<ID>, ID> List<D> findByIds(Class<D> tClass, Collection<ID> ids) {
        if (isUseNew) {
            return this.findByIdsNew(tClass, ids);
        }
        Table annotation = tClass.getAnnotation(Table.class);
        String table = annotation.name();
        return jdbcTemplate.queryForList("select * from " + table + " where id in (?)", tClass, ids);
    }

    public <D extends AbstractDO<ID>, ID> List<D> findByIdsNew(Class<D> tClass, Collection<ID> ids) {
        return this.findListByCondition(tClass, "where id in ( :ids )", SqlParam.create("ids", ids));
    }

    public <D extends AbstractDO<ID>, ID> D findById(Class<D> tClass, ID id) {
        if (isUseNew) {
            return findByIdNew(tClass, id);
        }
        Table annotation = tClass.getAnnotation(Table.class);
        String table = annotation.name();

        return jdbcTemplate.queryForObject("select * from " + table + " where id = ? ", (rs, rowNum) -> {
            try {
                D domain = tClass.newInstance();
                domain.setId(id);
                if (domain instanceof BaseDO) {
                    ((BaseDO<ID>) domain).setVer(rs.getInt("ver"));
                    ((BaseDO<ID>)domain).setCreateTime(new EzDate(rs.getTimestamp("create_time").toLocalDateTime()));
                    ((BaseDO<ID>)domain).setUpdateTime(new EzDate(rs.getTimestamp("update_time").toLocalDateTime()));
                }
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

    public <D extends AbstractDO<ID>, ID> D findByIdNew(Class<D> tClass, ID id) {
        return this.findByCondition(tClass, "where id = :id", SqlParam.create("id", id));
    }

    public <D extends AbstractDO<ID>, ID> D findByCondition(Class<D> tClass, String whereSql, SqlParam sqlParam) {
        try {
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return null;
            }
            whereSql = whereSql + " ";
            Map<String, Object> map = sqlParam.getData();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof String) {
                    whereSql = whereSql.replaceAll(":" + entry.getKey(), "\""+entry.getValue() + "\"");
                } else {
                    whereSql = whereSql.replaceAll(":" + entry.getKey(), entry.getValue().toString());
                }
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            return jdbcTemplate.queryForObject("select * from " + tableName + " " + whereSql,
                    new NamedPropertyRowMapper<>(tClass));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <D extends AbstractDO<ID>, ID> List<D> findListByCondition(Class<D> tClass, String whereSql, SqlParam sqlParam) {
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

    public <ID, D extends AbstractDO<ID>> boolean update(D domain) {
        try {
            Class<? extends AbstractDO> tClass = domain.getClass();
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return false;
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            Field[] fields = tClass.getDeclaredFields();
            List<String> updateSql = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object f = field.get(domain);
                if (f instanceof String) {
                    updateSql.add("`"+field.getAnnotation(Column.class).name()+"` = \""+f+"\"");
                } else {
                    updateSql.add("`"+field.getAnnotation(Column.class).name()+"` = "+f);
                }
                field.setAccessible(false);
            }
            String sql;
            if (domain instanceof BaseDO) {
                int newVer = ((BaseDO<?>) domain).getVer() + 1;
                sql = "update "+tableName+ " set " + EzString.join(updateSql, ",") + ", ver = "
                        +newVer+ " where id = "+domain.getId()+" and ver = "+((BaseDO<?>) domain).getVer();
            } else {
                sql = "update "+tableName+ " set " + EzString.join(updateSql, ",") + " where id = "+domain.getId();
            }
            return 1 == jdbcTemplate.update(sql);
        } catch (Exception e) {
            return false;
        }
    }

    public <ID, D extends AbstractDO<ID>> boolean save(D domain) {
        try {
            Class<? extends AbstractDO> tClass = domain.getClass();
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return false;
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            Field[] fields = tClass.getDeclaredFields();
            List<String> fieldNames = new ArrayList<>();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object f = field.get(domain);
                if (f instanceof String) {
                    fieldNames.add("`"+field.getAnnotation(Column.class).name()+"`");
                    fieldValues.add("\""+f+"\"");
                } else {
                    fieldNames.add("`"+field.getAnnotation(Column.class).name()+"`");
                    fieldValues.add(f);
                }
                field.setAccessible(false);
            }
            String sql;
            if (domain instanceof BaseDO) {
                sql = "insert into "+tableName+ " ( " + EzString.join(fieldNames, ",") + ", `ver`) values ("+
                        EzString.joins(fieldValues, ",")+", 1)";
            } else {
                sql = "insert into "+tableName+ " ( " + EzString.join(fieldNames, ",") + ") values ("+
                        EzString.joins(fieldValues, ",")+")";
            }
            return 1 == jdbcTemplate.update(sql);
        } catch (Exception e) {
            return false;
        }
    }

    public <ID, D extends AbstractDO<ID>> boolean delete(Class<D> tClass, ID id) {
        try {
            if (Objects.isNull(tClass.getAnnotation(Table.class))) {
                return false;
            }
            String tableName = tClass.getAnnotation(Table.class).name();
            String idField = id instanceof String ? "\""+id+"\"" : id.toString();
            String sql = "delete from "+tableName+" where id = "+ idField;
            return 1 == jdbcTemplate.update(sql);
        } catch (Exception e) {
            return false;
        }
    }
}
