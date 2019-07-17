package com.starmcc.qmdata.base;

import com.starmcc.qmdata.exception.QmDataDtoException;
import com.starmcc.qmdata.note.*;
import com.starmcc.qmdata.util.QmDataStyleTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author qm
 * @date 2018年11月24日 上午2:31:35
 * @Description 数据持久层封装DTO
 */
public final class QmDataDto<T> {

    private static final Logger LOG = LoggerFactory.getLogger(QmDataDto.class);

    private T bean;
    private boolean isPrimaryKey;
    private Style style;
    private String tableName;
    private String orderByValue;
    private Map<String, Object> primaryKey = null;
    private List<Map<String, Object>> params = null;

    private QmDataDto() {
    }

    /**
     * 构造一个Dto
     *
     * @param bean
     */
    protected QmDataDto(T bean, boolean isPrimaryKey) {
        this.bean = bean;
        this.isPrimaryKey = isPrimaryKey;
        Table table = this.bean.getClass().getAnnotation(Table.class);
        OrderBy orderBy = this.bean.getClass().getAnnotation(OrderBy.class);
        // 获取实体类风格
        if (table.style() == null) {
            this.style = Style.UNDERLINE;
        } else {
            this.style = table.style();
        }
        // 获取该实体的表名
        if (table != null && table.name() != null && !"".equals(table.name())) {
            this.tableName = table.name();
        } else {
            this.tableName = bean.getClass().getSimpleName();
            if (table.style() != null && style == Style.UNDERLINE) {
                this.tableName = QmDataStyleTools.transformNameByUnderline(this.tableName);
            }
        }
        // 获取orderby SQL语句
        if (orderBy == null || orderBy.value() == null || "".equals(orderBy.value())) {
            this.orderByValue = null;
        } else {
            this.orderByValue = orderBy.value();
        }
    }

    /**
     * 获取实体类参数封装Map
     *
     * @return
     */
    protected LinkedHashMap<String, Object> getParamsMap() {
        LinkedHashMap<String, Object> resMap = new LinkedHashMap<>();
        // 获取该实体的字段进行操作
        final Field[] fields = this.bean.getClass().getDeclaredFields();
        // 如果该字段为空则返回
        if (fields == null) {
            throw new QmDataDtoException("检测不到该实体的字段集!");
        }
        // 遍历字段进行参数封装
        for (Field field : fields) {
            // 开放字段权限public
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            // 判断是否需要主键策略
            if (isPrimaryKey && this.primaryKey == null) {
                // 序列化该主键
                if (setPrimaryKey(field)) {
                    continue;
                }
            }
            // 序列化该字段
            setFiledToList(field);
        }
        resMap.put("primaryKey", this.primaryKey);
        resMap.put("params", this.params);
        resMap.put("tableName", this.tableName);
        if (this.orderByValue != null) {
            resMap.put("orderBy", this.orderByValue);
        }
        LOG.debug("注入MyBatis数据：" + resMap.toString());
        return resMap;
    }

    /**
     * 设置主键
     *
     * @param id
     * @return
     */
    private boolean setPrimaryKey(Field filed) {
        Id idKey = filed.getAnnotation(Id.class);
        if (idKey == null) {
            return false;
        }
        Object obj = null;
        try {
            obj = filed.get(bean);
        } catch (IllegalAccessException e) {
            throw new QmDataDtoException("读取实体类主键字段发生了异常！");
        }
        if (obj != null) {
            // 不等于null
            // 判断是否设置别名
            this.primaryKey = new HashMap<>(16);
            if (idKey.name() == null || "".equals(idKey.name())) {
                String key = filed.getName();
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
        if (param != null && param.except()) {
            return;
        }
        Object obj = null;
        try {
            obj = field.get(bean);
            // 如果值是null则不需要这个值了。
            if (obj == null) {
                return;
            }
        } catch (IllegalAccessException e) {
            throw new QmDataDtoException("获取字段失败！");
        }
        // 开始获取字段并加入字段列表
        Map<String, Object> fieldMap = new HashMap<String, Object>(16);
        if (param == null || param.name() == null || "".equals(param.name())) {
            if (this.style == Style.UNDERLINE) {
                fieldMap.put("key", QmDataStyleTools.transformNameByUnderline(field.getName()));
            } else {
                fieldMap.put("key", field.getName());
            }
        } else {
            fieldMap.put("key", param.name());
        }
        fieldMap.put("value", obj);
        if (this.params == null) {
            this.params = new ArrayList<>();
        }
        this.params.add(fieldMap);
        return;
    }


}
