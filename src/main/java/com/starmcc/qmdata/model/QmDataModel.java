package com.starmcc.qmdata.model;

import com.starmcc.qmdata.exception.QmDataException;
import com.starmcc.qmdata.exception.QmDataModelException;
import com.starmcc.qmdata.note.*;
import com.starmcc.qmdata.util.QmDataStyleTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author starmcc
 * @version 2018年11月24日 上午2:31:35
 * 数据持久层封装Model
 */
public final class QmDataModel<Q> implements Serializable {

    private static final long serialVersionUID = 6857822692226391769L;
    private static final Logger LOG = LoggerFactory.getLogger(QmDataModel.class);
    private boolean isPrimaryKey;
    private Style style;
    private String tableName;
    private String whereSql;
    private String orderByValue;
    private Map<String, Object> primaryKey = null;
    private final List<Map<String, Object>> params = new ArrayList<>();
    private final Map<String, Object> paramsMap = new HashMap<>();


    public static <Q> QmDataModel<Q> getInstance(Q bean, boolean isPrimaryKey) {
        if (Objects.isNull(bean)) {
            return null;
        }
        return QmDataModel.getInstance(bean, null, null, isPrimaryKey, bean.getClass());
    }

    public static <Q> QmDataModel<Q> getInstance(Q bean, String whereSql, boolean isPrimaryKey) {
        if (Objects.isNull(bean)) {
            return null;
        }
        return QmDataModel.getInstance(bean, whereSql, null, isPrimaryKey, bean.getClass());
    }

    public static <Q> QmDataModel<Q> getInstance(Q query, String whereSql, String orderBy, boolean isPrimaryKey, Class<?> clamm) {
        clamm = Objects.isNull(clamm) ? query.getClass() : clamm;
        return new QmDataModel<Q>(query, whereSql, orderBy, isPrimaryKey, clamm);
    }


    private QmDataModel() {
    }

    /**
     * 构造一个Model
     *
     * @param query
     * @param whereSql
     * @param orderBy
     * @param isPrimaryKey
     * @param clamm
     */
    private QmDataModel(Q query, String whereSql, String orderBy, boolean isPrimaryKey, Class<?> clamm) {
        // 构建模型
        this.buildModel(query, whereSql, orderBy, isPrimaryKey, clamm);
        // 构建 paramsMap
        this.buildParamsMap();
    }

    /**
     * 获取实体类参数封装Map
     *
     * @return paramsMap
     */
    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }


    /**
     * 构建模型
     *
     * @param query
     * @param whereSql
     * @param orderBy
     * @param isPrimaryKey
     */
    private void buildModel(Q query, String whereSql, String orderByValue, boolean isPrimaryKey, Class<?> clamm) {
        if (Objects.isNull(query)) {
            query = this.buildBean(clamm);
        }
        this.isPrimaryKey = isPrimaryKey;
        Table table = clamm.getAnnotation(Table.class);
        // 获取该实体的表名
        this.tableName = Objects.nonNull(table) && StringUtils.hasText(table.name()) ? table.name() : clamm.getSimpleName();
        // 获取实体类风格
        this.style = Objects.nonNull(table) && Objects.nonNull(table.style()) ? table.style() : Style.UNDERLINE;
        if (style == Style.UNDERLINE) {
            // 转换风格
            this.tableName = QmDataStyleTools.transformNameByUnderline(this.tableName);
        }
        this.whereSql = whereSql;
        OrderBy orderBy = query.getClass().getAnnotation(OrderBy.class);
        // 获取orderby SQL语句
        if (StringUtils.hasText(orderByValue)) {
            // 如果不等于空，则直接使用参数列表提供的orderBy语句
            this.orderByValue = orderByValue;
        } else if (Objects.nonNull(orderBy) && StringUtils.hasText(orderBy.value())) {
            // 如果参数列表的orderBy 等于空，且实体类中的OrderBy注解不为空，则直接使用orderBy注解提供的语句。
            this.orderByValue = orderBy.value();
        }

        // 获取该实体的字段进行操作
        final Field[] fields = query.getClass().getDeclaredFields();
        // 如果该字段为空则返回
        if (Objects.isNull(fields)) {
            throw new QmDataModelException("检测不到该实体的字段集!");
        }
        // 遍历字段进行参数封装
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                // 不是私有的，或是静态的，或是常量的，都不进行序列化
                continue;
            }
            // 开放字段private权限
            field.setAccessible(true);
            // 判断是否需要主键策略
            if (isPrimaryKey && Objects.isNull(this.primaryKey)) {
                // 序列化该主键
                if (this.setPrimaryKey(query, field)) {
                    continue;
                }
            }
            // 序列化该字段
            this.setFiledToList(query, field);
        }
    }

    /**
     * 构建 params Map
     */
    private void buildParamsMap() {
        paramsMap.put("primaryKey", this.primaryKey);
        paramsMap.put("params", this.params);
        paramsMap.put("tableName", this.tableName);
        paramsMap.put("whereSql", this.whereSql);
        paramsMap.put("orderBy", this.orderByValue);
        LOG.debug("注入MyBatis数据：" + paramsMap.toString());
    }

    /**
     * 设置主键
     *
     * @param id
     * @return
     */
    private boolean setPrimaryKey(Q bean, Field field) {
        Id idKey = field.getAnnotation(Id.class);
        if (Objects.isNull(idKey)) {
            return false;
        }
        Object obj = null;
        try {
            obj = field.get(bean);
        } catch (IllegalAccessException e) {
            throw new QmDataModelException("读取实体类主键字段发生了异常！", e);
        }
        if (Objects.nonNull(obj)) {
            // 不等于null
            // 判断是否设置别名
            this.primaryKey = new HashMap<>(16);
            if (!StringUtils.hasText(idKey.name())) {
                String key = field.getName();
                if (this.style == Style.UNDERLINE) {
                    this.primaryKey.put("key", QmDataStyleTools.transformNameByUnderline(key));
                } else {
                    this.primaryKey.put("key", key);
                }
            } else {
                this.primaryKey.put("key", idKey.name());
            }
            this.primaryKey.put("value", obj);
        }
        return true;
    }


    /**
     * 设置字段
     *
     * @param field
     * @return
     */
    private void setFiledToList(Q bean, Field field) {
        Param param = field.getAnnotation(Param.class);
        // 判断是否有该注解，如果存在并且except等于true则不加入该字段。
        if (Objects.nonNull(param) && param.except()) {
            return;
        }
        Object obj = null;
        try {
            obj = field.get(bean);
            // 如果值是null则不需要这个值了。
            if (Objects.isNull(obj)) {
                return;
            }
        } catch (IllegalAccessException e) {
            throw new QmDataModelException("获取字段失败！");
        }
        // 开始获取字段并加入字段列表
        Map<String, Object> fieldMap = new HashMap<String, Object>(16);
        if (Objects.isNull(param) || !StringUtils.hasText(param.name())) {
            if (this.style == Style.UNDERLINE) {
                fieldMap.put("key", QmDataStyleTools.transformNameByUnderline(field.getName()));
            } else {
                fieldMap.put("key", field.getName());
            }
        } else {
            fieldMap.put("key", param.name());
        }
        fieldMap.put("value", obj);
        this.params.add(fieldMap);
        return;
    }

    /**
     * 判断实体类是否为空，为空则自动创建新的对象。
     *
     * @param bean
     * @param clamm
     * @return
     */
    private <Q> Q buildBean(Class<?> clamm) {
        try {
            return (Q) clamm.newInstance();
        } catch (Exception e) {
            throw new QmDataException("传递实体为空，且自动创建失败!");
        }
    }

}
