package com.starmcc.qmdata.common;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * @Author: qm
 * @Date: 2019/5/26 15:26
 * @Description: 创建QmData实例
 */
public class QmDataFactory {

    private QmDataFactory() {
    }

    /**
     * 获取QmData实例
     *
     * @param sqlSessionFactory Mybatis依赖的SqlSessionFactory
     * @return
     */
    public static QmData createInstance(SqlSessionTemplate sqlSessionTemplate) {
        return new QmDataEntrance(sqlSessionTemplate);
    }

}
