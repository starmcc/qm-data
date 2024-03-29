package com.starmcc.qmdata.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author starmcc
 * @version 2019/1/23 11:22
 * 风格转换工具
 */
public final class QmDataStyleTools {

    private QmDataStyleTools() {
    }

    ;

    /**
     * 正则-识别大写
     */
    private final static String PATTERN_1 = "[A-Z]";

    /**
     * 正则-识别下划线+字母
     */
    private final static String PATTERN_2 = "[_][A-Za-z]";

    /**
     * 使用递归模式 转换map中的key名样式
     *
     * @param map
     * @return map
     */
    public final static Map transformMapForHump(Map map) {
        Iterator iter = map.entrySet().iterator();
        Map resMap = new HashMap(16);
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            key = transformNameByHump(key);
            if (Objects.nonNull(val)
                    && !val.getClass().isPrimitive()
                    && !isPackDataTypes(val.getClass())) {
                Map mapTemp = (Map) val;
                val = transformMapForHump(mapTemp);
            }
            resMap.put(key, val);
        }
        return resMap;
    }

    /**
     * 是否为包装数据类型
     *
     * @param clazz class
     * @return
     */
    private final static boolean isPackDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        classSet.add(String.class);
        classSet.add(Timestamp.class);
        classSet.add(Date.class);
        classSet.add(BigDecimal.class);
        return classSet.contains(clazz);
    }


    /**
     * 转换风格 驼峰转下划线
     *
     * @param fieldName 属性名称
     * @return fieldName
     */
    public final static String transformNameByUnderline(String fieldName) {
        fieldName = toLowerCaseFirstOne(fieldName);
        Pattern pattern = Pattern.compile(PATTERN_1);
        Matcher matcher = pattern.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    private static String toLowerCaseFirstOne(String s) {
        if (!Character.isLowerCase(s.charAt(0))) {
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toLowerCase(s.charAt(0)));
            sb.append(s.substring(1));
            s = sb.toString();
        }
        return s;
    }

    /**
     * 转换风格 下划线转驼峰
     *
     * @param fieldName 属性名称
     * @return fieldName
     */
    public final static String transformNameByHump(String fieldName) {
        Pattern pattern = Pattern.compile(PATTERN_2);
        Matcher matcher = pattern.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).substring(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
