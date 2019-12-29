package com.starmcc.qmdata.common;

import com.starmcc.qmdata.model.ResultInsert;

import java.util.List;

/**
 * @author qm
 * @date 2018年11月24日 上午2:16:16
 * @Description Mybatis数据持久层封装工具
 */
public interface QmData {

    /**
     * 通用查询列表
     *
     * @param entity 对应表的实体类(必须带有@Table)
     * @param clamm  对应表的实体类的Class
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm);

    /**
     * 通用查询单条记录
     *
     * @param entity 实体类(必须带有@Table)
     * @param clamm  实体类class对象
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> Q autoSelectOne(Q entity, Class<Q> clamm);


    /**
     * 通用插入记录
     *
     * @param entity 对应表的实体类(必须带有@Table和@Id)
     * @return 影响行数
     */
    <Q> int autoInsert(Q entity);

    /**
     * 通用插入记录 （返回主键）
     *
     * @param entity 对应表的实体类(必须带有@Table和@Id)
     * @return ResultInsert 返回带新增主键id的对象
     */
    <Q> ResultInsert autoInsertGetPrimaryKey(Q entity);

    /**
     * 通用修改记录
     *
     * @param entity 对应表的实体类(必须带有@Table和@Id)
     * @return 影响行数
     */
    <Q> int autoUpdate(Q entity);


    /**
     * 通用删除记录
     *
     * @param entity 对应表的实体类(必须带有@Table)
     * @return 影响行数
     */
    <Q> int autoDelete(Q entity);


    /**
     * 通用查询记录数
     *
     * @param entity 对应表的实体类(必须带有@Table)
     * @return 影响行数
     */
    <Q> int autoSelectCount(Q entity);

    // --------------where 重载------------------


    /**
     * 通用查询列表
     *
     * @param entity 对应表的实体类(必须带有@Table)
     * @param where  条件sql
     * @param clamm  对应表的实体类的Class
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> List<Q> autoSelectList(Q entity, String where, Class<Q> clamm);


    /**
     * 通用查询单条记录
     *
     * @param entity 实体类(必须带有@Table)
     * @param where  条件sql
     * @param clamm  实体类class对象
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> Q autoSelectOne(Q entity, String where, Class<Q> clamm);


    /**
     * 通用修改记录
     *
     * @param entity 对应表的实体类(必须带有@Table和@Id)
     * @param where  条件sql
     * @return 影响行数
     */
    <Q> int autoUpdate(Q entity, String where);


    /**
     * 通用删除记录
     *
     * @param entity 对应表的实体类(必须带有@Table)
     * @param where  条件sql
     * @return 影响行数
     */
    <Q> int autoDelete(Q entity, String where);


    /**
     * 通用查询记录数
     *
     * @param entity   对应表的实体类(必须带有@Table)
     * @param whereSql 条件sql
     * @param <Q>
     * @return 影响行数
     */
    <Q> int autoSelectCount(Q entity, String whereSql);

    // -------------where orderby 重载 --------------------

    /**
     * 通用查询列表
     *
     * @param entity  对应表的实体类(必须带有@Table)
     * @param where   条件sql
     * @param orderBy 排序sql
     * @param clamm   对应表的实体类的Class
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> List<Q> autoSelectList(Q entity, String where, String orderBy, Class<Q> clamm);


    /**
     * 通用查询单条记录
     *
     * @param entity  实体类(必须带有@Table)
     * @param where   条件sql
     * @param orderBy 排序sql
     * @param clamm   实体类class对象
     * @return 根据参数指定的类型进行嵌套数据
     */
    <Q> Q autoSelectOne(Q entity, String where, String orderBy, Class<Q> clamm);

    // -----------1.x 兼容接口 -------------

    /**
     * 查询列表
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 根据返回指定的类型进行嵌套数据
     */
    @Deprecated
    <Q> List<Q> selectList(String sqlName, Object params);

    /**
     * 查询单条记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 根据返回指定的类型进行嵌套数据
     */
    @Deprecated
    <Q> Q selectOne(String sqlName, Object params);


    /**
     * 插入记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    @Deprecated
    int insert(String sqlName, Object params);

    /**
     * 修改记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    @Deprecated
    int update(String sqlName, Object params);

    /**
     * 删除记录
     *
     * @param sqlName 命名空间
     * @param params  参数
     * @return 影响行数
     */
    @Deprecated
    int delete(String sqlName, Object params);

}
