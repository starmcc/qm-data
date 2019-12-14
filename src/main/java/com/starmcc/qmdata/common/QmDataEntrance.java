package com.starmcc.qmdata.common;

import com.starmcc.qmdata.base.QmDataImplement;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @Author: qm
 * @Date: 2019/5/26 15:49
 * @Description: QmBase入口封装
 */
public class QmDataEntrance extends QmDataImplement {

    /**
     * 禁止无参实例化
     */
    private QmDataEntrance() {}

    /**
     * 初始化父类 sqlSessionFactory
     * @param sqlSessionFactory
     */
    public QmDataEntrance(SqlSessionTemplate sqlSessionTemplate) {
        super.sqlSessionTemplate = sqlSessionTemplate;
    }
}
