//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.poethan.jear.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.poethan.jear.utils.EzDate;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.persistence.Column;

public class NamedPropertyRowMapper<T extends AbstractDO<ID>, ID> implements RowMapper<T> {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private Class<T> mappedClass;

    private boolean checkFullyPopulated = false;
    private boolean primitivesDefaultedForNullValue = false;
    @Nullable
    private ConversionService conversionService = DefaultConversionService.getSharedInstance();
    @Nullable
    private Map<String, PropertyDescriptor> mappedFields;
    @Nullable
    private Set<String> mappedProperties;

    public NamedPropertyRowMapper() {
    }

    public NamedPropertyRowMapper(Class<T> mappedClass) {
        this.initialize(mappedClass);
    }

    public NamedPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        this.initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            this.initialize(mappedClass);
        } else if (this.mappedClass != mappedClass) {
            throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + mappedClass + " since it is already providing mapping for " + this.mappedClass);
        }

    }

    @Nullable
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    public void setConversionService(@Nullable ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Nullable
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<>();
        this.mappedProperties = new HashSet<>();
        PropertyDescriptor[] var2 = BeanUtils.getPropertyDescriptors(mappedClass);
        int var3 = var2.length;

        for (PropertyDescriptor pd : var2) {
            if (pd.getWriteMethod() != null) {
                String lowerCaseName = this.lowerCaseName(pd.getName());
                this.mappedFields.put(lowerCaseName, pd);
                String underscoreName = this.underscoreName(pd.getName());
                if (!lowerCaseName.equals(underscoreName)) {
                    this.mappedFields.put(underscoreName, pd);
                }

                this.mappedProperties.add(pd.getName());
            }
        }

    }

    protected void suppressProperty(String propertyName) {
        if (this.mappedFields != null) {
            this.mappedFields.remove(this.lowerCaseName(propertyName));
            this.mappedFields.remove(this.underscoreName(propertyName));
        }

    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            result.append(Character.toLowerCase(name.charAt(0)));

            for(int i = 1; i < name.length(); ++i) {
                char c = name.charAt(i);
                if (Character.isUpperCase(c)) {
                    result.append('_').append(Character.toLowerCase(c));
                } else {
                    result.append(c);
                }
            }

            return result.toString();
        }
    }

    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        T mappedObject = BeanUtils.instantiateClass(this.mappedClass);
        Object ID =  rs.getObject("id");
        this.assignId(mappedObject, ID);
        if (mappedObject instanceof BaseDO) {
            ((BaseDO)mappedObject).setVer(rs.getInt("ver"));
            if (rs.getObject("create_time") instanceof Integer) {
                int timpStamp = (Integer) rs.getObject("create_time");
                ((BaseDO)mappedObject).setCreateTime(new EzDate(timpStamp));
            } else if (rs.getObject("create_time") instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) rs.getObject("create_time");
                ((BaseDO)mappedObject).setCreateTime(new EzDate(timestamp.toLocalDateTime()));
            }
            if (rs.getObject("update_time") instanceof Integer) {
                int timpStamp = (Integer) rs.getObject("update_time");
                ((BaseDO)mappedObject).setUpdateTime(new EzDate(timpStamp));
            } else if (rs.getObject("update_time") instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) rs.getObject("update_time");
                ((BaseDO)mappedObject).setUpdateTime(new EzDate(timestamp.toLocalDateTime()));
            }
        }
        try{
            Field[] fields = mappedClass.getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);

                if (field.getType().isAssignableFrom(EzDate.class)) {
                    this.assignEzDate(field, mappedObject, rs.getObject(column.name()));
                } else {
                    field.set(mappedObject, rs.getObject(column.name()));
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException illegalAccessException) {
            if(this.logger.isDebugEnabled()) {
                this.logger.debug(illegalAccessException.getMessage());
            }
        }
        return mappedObject;
    }

    @SneakyThrows
    private void assignId(T mappedObject, Object ID) {
        ParameterizedType type = (ParameterizedType)mappedObject.getClass().getGenericSuperclass();
        String idType = type.getActualTypeArguments()[0].getTypeName();
        Field field;
        if (mappedObject instanceof BaseDO) {
            field = mappedObject.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
        } else {
            field = mappedObject.getClass().getSuperclass().getDeclaredField("id");
        }
        field.setAccessible(true);
        if(Long.class.getName().equals(idType)) {
            field.set(mappedObject, (Long.parseLong(ID.toString())));
        } else if (Integer.class.getName().equals(idType)) {
            field.set(mappedObject, (Integer.parseInt(ID.toString())));
        } else if (String.class.getName().equals(idType)) {
            field.set(mappedObject, ID.toString());
        } else {
            throw new RuntimeException();
        }
        field.setAccessible(false);
    }

    private void assignEzDate(Field field, T mappedObject, Object value) throws IllegalAccessException {
        if (value instanceof Integer
                || value instanceof Long
                || value instanceof LocalDateTime
                || value instanceof String) {
            field.set(mappedObject, value);
        }
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        ConversionService cs = this.getConversionService();
        if (cs != null) {
            bw.setConversionService(cs);
        }

    }

    @Nullable
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    @Nullable
    protected Object getColumnValue(ResultSet rs, int index, Class<?> paramType) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, paramType);
    }

  /*  public static NamedPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        return new NamedPropertyRowMapper(mappedClass);
    }

    public static <T> NamedPropertyRowMapper<T> newInstance(Class<T> mappedClass, @Nullable ConversionService conversionService) {
        NamedPropertyRowMapper<T> rowMapper = newInstance(mappedClass);
        rowMapper.setConversionService(conversionService);
        return rowMapper;
    }*/
}
