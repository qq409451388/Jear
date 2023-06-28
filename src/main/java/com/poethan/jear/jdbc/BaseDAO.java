package com.poethan.jear.jdbc;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Repository
abstract public class BaseDAO<ID, D extends AbstractDO<ID>> {
    @Resource
    private JdbcDAO jdbcDAO;
    private Class<D> domainClass;

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
                }
            }
        }
    }

    public D findById(ID id) {
        return this.jdbcDAO.findById(domainClass, id);
    }
}
