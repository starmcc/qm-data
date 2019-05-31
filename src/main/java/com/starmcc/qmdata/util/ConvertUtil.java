package com.starmcc.qmdata.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author qm
 * @date 2018年11月24日 上午1:57:19
 * @Description List、Map工具类
 */
public final class ConvertUtil {

    private ConvertUtil() {
    }

    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public final static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return
     */
    public final static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public final static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List <Map<String,Object>>转换为List
     *
     * @param maps
     * @param clazz
     * @return
     */
    public final static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                try {
                    bean = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }
}
