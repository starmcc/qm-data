package com.starmcc.qmdata.common;

import com.starmcc.qmdata.model.ResultInsert;

import java.util.List;

/**
 * @author starmcc
 * @version 2018年11月24日 上午2:16:16
 * Mybatis数据持久层封装工具
 */
public interface QmData {

    /**
     * 通用查询列表
     *
     * @param entity 对应表的实体类
     * @param clamm  对应表的实体类的Class (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> List<M> autoSelectList(Q entity, Class<M> clamm);

    /**
     * 通用查询单条记录
     *
     * @param entity 实体类
     * @param clamm  实体类class对象 (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> M autoSelectOne(Q entity, Class<M> clamm);

    /**
     * 通用插入记录
     *
     * @param entity 对应表的实体类 (以该对象作表名解析，必须带有@Id)
     * @return 影响行数
     */
    <Q> int autoInsert(Q entity);

    /**
     * 通用插入记录 （返回主键）
     *
     * @param entity 对应表的实体类 (以该对象作表名解析，必须带有@Id)
     * @return ResultInsert 返回带新增主键id的对象
     */
    <Q> ResultInsert autoInsertGetPrimaryKey(Q entity);

    /**
     * 通用修改记录
     *
     * @param entity 对应表的实体类 (以该对象作表名解析，必须带有@Id)
     * @return 影响行数
     */
    <Q> int autoUpdate(Q entity);


    /**
     * 通用删除记录
     *
     * @param entity 对应表的实体类 (以该对象作表名解析)
     * @return 影响行数
     */
    <Q> int autoDelete(Q entity);


    /**
     * 通用查询记录数
     *
     * @param entity 对应表的实体类
     * @param clamm  实体类class对象 (以该对象作表名解析)
     * @return 影响行数
     */

    <Q, M> Long autoSelectCount(Q entity, Class<M> clamm);


    /**
     * 通用修改记录
     *
     * @param entity 对应表的实体类 (以该对象作表名解析,必须带有@Id)
     * @param where  条件sql
     * @return 影响行数
     */
    <Q> int autoUpdate(Q entity, String where);


    /**
     * 通用删除记录
     *
     * @param entity 对应表的实体类 (以该对象作表名解析)
     * @param where  条件sql
     * @return 影响行数
     */
    <Q> int autoDelete(Q entity, String where);

    // -------------where orderby 重载 --------------------

    /**
     * 通用查询记录数
     *
     * @param entity   对应表的实体类
     * @param whereSql 条件sql
     * @param clamm    实体类class对象 (以该对象作表名解析)
     * @return 影响行数
     */
    <Q, M> Long autoSelectCount(Q entity, String whereSql, Class<M> clamm);


    /**
     * 通用查询列表
     *
     * @param entity  对应表的实体类
     * @param where   条件sql
     * @param orderBy 排序sql
     * @param clamm   对应表的实体类的Class (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> List<M> autoSelectList(Q entity, String where, String orderBy, Class<M> clamm);


    /**
     * 通用查询列表
     *
     * @param entity 对应表的实体类
     * @param where  条件sql
     * @param clamm  对应表的实体类的Class (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> List<M> autoSelectList(Q entity, String where, Class<M> clamm);


    /**
     * 通用查询列表
     *
     * @param where 条件sql
     * @param clamm 对应表的实体类的Class (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <M> List<M> autoSelectList(String where, Class<M> clamm);

    /**
     * 通用查询单条记录
     *
     * @param entity  实体类
     * @param where   条件sql
     * @param orderBy 排序sql
     * @param clamm   实体类class对象 (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> M autoSelectOne(Q entity, String where, String orderBy, Class<M> clamm);

    /**
     * 通用查询单条记录
     *
     * @param entity 实体类
     * @param where  条件sql
     * @param clamm  实体类class对象 (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q, M> M autoSelectOne(Q entity, String where, Class<M> clamm);


    /**
     * 通用查询单条记录
     *
     * @param where 条件sql
     * @param clamm 实体类class对象 (以该对象作表名解析)
     * @return 根据参数指定的类型进行嵌套数据
     */
    <M> M autoSelectOne(String where, Class<M> clamm);
}
