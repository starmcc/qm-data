package com.starmcc.qmdata.model;

import com.starmcc.qmdata.exception.QmDataDtoException;
import com.starmcc.qmdata.note.*;
import com.starmcc.qmdata.util.QmDataStyleTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author qm
 * @date 2018年11月24日 上午2:31:35
 * @Description 数据持久层封装Model
 */
public final class QmDataModel<T> implements Serializable {

    private static final long serialVersionUID = 6857822692226391769L;
    private static final Logger LOG = LoggerFactory.getLogger(QmDataModel.class);

    private T bean;
    private boolean isPrimaryKey;
    private Style style;
    private String tableName;
    private String orderByValue;
    private Map<String, Object> primaryKey = null;
    private List<Map<String, Object>> params = null;
    private LinkedHashMap<String, Object> paramsMap = null;

    private QmDataModel() {
    }

    public static <T> QmDataModel<T> getInstance(T bean, boolean isPrimaryKey) {
        return new QmDataModel<T>(bean, isPrimaryKey);
    }

    /**
     * 构造一个Dto
     *
     * @param bean
     */
    private QmDataModel(T bean, boolean isPrimaryKey) {
        if (null == bean) {
            return;
        }
        this.bean = bean;
        this.isPrimaryKey = isPrimaryKey;
        Table table = this.bean.getClass().getAnnotation(Table.class);
        OrderBy orderBy = this.bean.getClass().getAnnotation(OrderBy.class);
        // 获取实体类风格
        if (null == table.style()) {
            this.style = Style.UNDERLINE;
        } else {
            this.style = table.style();
        }
        // 获取该实体的表名
        if (null != table && null != table.name() && !"".equals(table.name())) {
            this.tableName = table.name();
        } else {
            this.tableName = this.bean.getClass().getSimpleName();
            if (null != table.style() && style == Style.UNDERLINE) {
                this.tableName = QmDataStyleTools.transformNameByUnderline(this.tableName);
            }
        }
        // 获取orderby SQL语句
        if (null == orderBy || null == orderBy.value() || "".equals(orderBy.value())) {
            this.orderByValue = null;
        } else {
            this.orderByValue = orderBy.value();
        }
        // 构建 paramsMap
        this.buildParamsMap();
    }

    /**
     * 获取实体类参数封装Map
     *
     * @return
     */
    public LinkedHashMap<String, Object> getParamsMap() {
        return paramsMap;
    }

    /**
     * 构建 params Map
     */
    private void buildParamsMap() {
        paramsMap = new LinkedHashMap<>();
        // 获取该实体的字段进行操作
        final Field[] fields = this.bean.getClass().getDeclaredFields();
        // 如果该字段为空则返回
        if (null == fields) {
            throw new QmDataDtoException("检测不到该实体的字段集!");
        }
        // 遍历字段进行参数封装
        for (Field field : fields) {
            // 开放字段权限public
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            // 判断是否需要主键策略
            if (isPrimaryKey && null == this.primaryKey) {
                // 序列化该主键
                if (this.setPrimaryKey(field)) {
                    continue;
                }
            }
            // 序列化该字段
            this.setFiledToList(field);
        }
        paramsMap.put("primaryKey", this.primaryKey);
        paramsMap.put("params", this.params);
        paramsMap.put("tableName", this.tableName);
        if (this.orderByValue != null) {
            paramsMap.put("orderBy", this.orderByValue);
        }
        LOG.debug("注入MyBatis数据：" + paramsMap.toString());
    }

    /**
     * 设置主键
     *
     * @param id
     * @return
     */
    private boolean setPrimaryKey(Field field) {
        Id idKey = field.getAnnotation(Id.class);
        if (null == idKey) {
            return false;
        }
        Object obj = null;
        try {
            obj = field.get(bean);
        } catch (IllegalAccessException e) {
            throw new QmDataDtoException("读取实体类主键字段发生了异常！", e);
        }
        if (null != obj) {
            // 不等于null
            // 判断是否设置别名
            this.primaryKey = new HashMap<>(16);
            if (null == idKey.name() || "".equals(idKey.name())) {
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
    private void setFiledToList(Field field) {
        Param param = field.getAnnotation(Param.class);
        // 判断是否有该注解，如果存在并且except等于true则不加入该字段。
        if (null != param && param.except()) {
            return;
        }
        Object obj = null;
        try {
            obj = field.get(bean);
            // 如果值是null则不需要这个值了。
            if (null == obj) {
                return;
            }
        } catch (IllegalAccessException e) {
            throw new QmDataDtoException("获取字段失败！");
        }
        // 开始获取字段并加入字段列表
        Map<String, Object> fieldMap = new HashMap<String, Object>(16);
        if (null == param || null == param.name() || "".equals(param.name())) {
            if (this.style == Style.UNDERLINE) {
                fieldMap.put("key", QmDataStyleTools.transformNameByUnderline(field.getName()));
            } else {
                fieldMap.put("key", field.getName());
            }
        } else {
            fieldMap.put("key", param.name());
        }
        fieldMap.put("value", obj);
        if (null == this.params) {
            this.params = new ArrayList<>();
        }
        this.params.add(fieldMap);
        return;
    }


}
