package com.starmcc.qmdata.util;

import com.starmcc.qmdata.exception.QmDataException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map and Bean Tools
 *
 * @Author: qm
 * @Date: 2019/7/17 17:33
 */
public class QmConvertUtil {
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param map  包含属性值的 map
     * @param type 要转化的类型
     * @return 转化出来的 JavaBean 对象
     */
    public static <T> T mapToBean(Map map, Class<T> clazz) {
        try {
            // 获取类属性
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            // 创建 JavaBean 对象
            T obj = clazz.newInstance();
            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
            return obj;
        } catch (IntrospectionException e) {
            throw new QmDataException("IntrospectionException 分析类属性失败!");
        } catch (InstantiationException e) {
            throw new QmDataException("IllegalAccessException 实例化 JavaBean 失败!");
        } catch (IllegalAccessException e) {
            throw new QmDataException("IllegalAccessException 实例化 JavaBean 失败!");
        } catch (InvocationTargetException e) {
            throw new QmDataException("InvocationTargetException 调用属性的 setter 方法失败!");
        }
    }

    /**
     * 将一个 List 中包含的 map 集合转化为一个 List 中包含的 JavaBean 集合
     *
     * @param maps 包含属性值的 mapList
     * @param type 要转化的类型
     * @param <T>  转化出来的 JavaBean 对象 List
     * @return
     */
    public static <T> List<T> mapsToBeans(List<Map> maps, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (Map map : maps) {
            T t = mapToBean(map, clazz);
            list.add(t);
        }
        return list;
    }


    /**
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     */
    public static Map beanToMap(Object bean) {
        try {
            Class type = bean.getClass();
            Map returnMap = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
            return returnMap;
        } catch (IntrospectionException e) {
            throw new QmDataException("IntrospectionException 分析类属性失败!");
        } catch (IllegalAccessException e) {
            throw new QmDataException("IllegalAccessException 实例化 JavaBean 失败!");
        } catch (InvocationTargetException e) {
            throw new QmDataException("InvocationTargetException 调用属性的 setter 方法失败!");
        }
    }

    /**
     * 将一个 List 包含 JavaBean 的集合转化为一个 List 包含 map 的集合
     *
     * @param beans 要转化的 list<JavaBean>
     * @return
     */
    public static <T> List<Map> beansToMaps(List<T> beans) {
        List<Map> list = new ArrayList<>();
        for (T bean : beans) {
            Map map = beanToMap(bean);
            list.add(map);
        }
        return list;
    }
}
