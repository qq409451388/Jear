package com.poethan.jear.jdbc;

import com.poethan.jear.module.cache.EzRedis;
import com.poethan.jear.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
abstract public class BaseDAO<ID, D extends AbstractDO<ID>> {
    @Resource
    private JdbcDAO jdbcDAO;
    @Resource
    private EzRedis redis;

    private Class<D> domainClass;
    private Class<ID> domainIdClass;

    public BaseDAO() {
        Type superClass = getClass().getGenericSuperclass();
        // 判断是否为参数化类型
        if (superClass instanceof ParameterizedType) {
            // 获取参数化类型的实际类型参数数组
            Type[] typeArgs = ((ParameterizedType) superClass).getActualTypeArguments();

            // 获取第二个参数，即D的类型参数
            if (typeArgs != null && typeArgs.length >= 2) {
                Type entityIdType = typeArgs[0];
                Type entityType = typeArgs[1];

                // 判断类型是否为Class类型
                if (entityType instanceof Class) {
                    domainClass = (Class<D>) entityType;
                    domainIdClass = (Class<ID>) entityIdType;
                }
            }
        }
    }

    public D findById(ID id) {
        D domain = JsonUtils.decode(redis.get(getDomainCacheKey(id)), domainClass);
        if (Objects.nonNull(domain)) {
            return domain;
        }
        domain = this.jdbcDAO.findById(domainClass, id);
        redis.setEx(getDomainCacheKey(id), JsonUtils.encode(domain), 86400);
        return domain;
    }

    public List<D> findByIds(Collection<ID> ids) {
        return this.jdbcDAO.findByIds(domainClass, ids);
    }

    public boolean update(D domain) {
        boolean updateRes = this.jdbcDAO.update(domain);
        if (updateRes) {
            this.redis.del(getDomainCacheKey(domain.getId()));
        }
        return updateRes;
    }

    public boolean save(D domain) {
        return this.jdbcDAO.save(domain);
    }

    private String getDomainCacheKey(ID id) {
        return this.domainClass.getSimpleName()+"::"+id;
    }

    public List<D> findByConditions(String whereSql, SqlParam sqlParam) {
        return jdbcDAO.findListByCondition(domainClass, whereSql, sqlParam);
    }
}
