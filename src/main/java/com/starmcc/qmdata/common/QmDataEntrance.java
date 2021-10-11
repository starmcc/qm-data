package com.starmcc.qmdata.common;

import com.starmcc.qmdata.base.AbstractQmDataAutoReload;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author starmcc
 * @version 2019/5/26 15:49
 * QmBase入口封装
 */
public class QmDataEntrance extends AbstractQmDataAutoReload {

    /**
     * 禁止无参实例化
     */
    private QmDataEntrance() {
    }

    /**
     * 初始化父类 sqlSessionFactory
     *
     * @param sqlSessionTemplate
     */
    public QmDataEntrance(SqlSessionTemplate sqlSessionTemplate) {
        super.sqlSessionTemplate = sqlSessionTemplate;
    }
}
