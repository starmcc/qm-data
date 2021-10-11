package com.starmcc.qmdata.common;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author starmcc
 * @version 2019/5/26 15:26
 * 创建QmData实例
 */
public class QmDataFactory {

    private QmDataFactory() {
    }

    /**
     * 获取QmData实例
     *
     * @param sqlSessionTemplate Mybatis依赖的SqlSessionFactory
     * @return QmDataEntrance
     */
    public static QmData createInstance(SqlSessionTemplate sqlSessionTemplate) {
        return new QmDataEntrance(sqlSessionTemplate);
    }

}
