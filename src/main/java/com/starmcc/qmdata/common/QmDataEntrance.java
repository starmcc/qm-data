package com.starmcc.qmdata.common;

import com.starmcc.qmdata.base.AbstractQmDataBase;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @Author: qm
 * @Date: 2019/5/26 15:49
 * @Description: QmBase入口封装
 */
public class QmDataEntrance extends AbstractQmDataBase {

    private QmDataEntrance() {
    }

    public QmDataEntrance(SqlSessionFactory sqlSessionFactory) {
        super.sqlSessionFactory = sqlSessionFactory;
    }
}
