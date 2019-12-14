package com.starmcc.qmdata.base;

import com.starmcc.qmdata.common.QmData;
import com.starmcc.qmdata.config.QmDataConstant;
import com.starmcc.qmdata.exception.QmDataException;
import com.starmcc.qmdata.model.QmDataModel;
import com.starmcc.qmdata.model.ResultInsert;
import com.starmcc.qmdata.note.Style;
import com.starmcc.qmdata.note.Table;
import com.starmcc.qmdata.util.QmConvertUtil;
import com.starmcc.qmdata.util.QmDataStyleTools;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qm
 * @Title QmBase实现类
 * @Date 注释时间：2019/1/23 12:43
 */
public class QmDataImplement implements QmData {

    private static final Logger LOG = LoggerFactory.getLogger(QmDataImplement.class);

    /**
     * 获取Mybatis SqlSession
     */
    protected SqlSessionTemplate sqlSessionTemplate;

    @Override
    public <M> List<M> selectList(String nameSpace, Object params) {
        try {
            final long time = System.currentTimeMillis();
            List<M> list = sqlSessionTemplate.selectList(nameSpace, params);
            LOG.info("selectList elapsed time:{}/ms", System.currentTimeMillis() - time);
            return list;
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
    }

    @Override
    public <M> M selectOne(String nameSpace, Object params) {
        try {
            final long time = System.currentTimeMillis();
            M obj = sqlSessionTemplate.selectOne(nameSpace, params);
            LOG.info("selectOne elapsed time:{}/ms", System.currentTimeMillis() - time);
            return obj;
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
    }

    @Override
    public int insert(String nameSpace, Object params) {
        try {
            final long time = System.currentTimeMillis();
            int result = sqlSessionTemplate.insert(nameSpace, params);
            LOG.info("insert elapsed time:{}/ms", System.currentTimeMillis() - time);
            return result;
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
    }

    @Override
    public int update(String nameSpace, Object params) {
        try {
            final long time = System.currentTimeMillis();
            int result = sqlSessionTemplate.update(nameSpace, params);
            LOG.info("update elapsed time:{}/ms", System.currentTimeMillis() - time);
            return result;
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
    }

    @Override
    public int delete(String nameSpace, Object params) {
        try {
            final long time = System.currentTimeMillis();
            int result = sqlSessionTemplate.delete(nameSpace, params);
            LOG.info("delete elapsed time:{}/ms", System.currentTimeMillis() - time);
            return result;
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
    }

    // =======================================华丽的分割线===========================================

    @Override
    public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, false);
        List<Map> mapLis = null;
        List<Q> list = null;
        try {
            // 执行SQL
            mapLis = sqlSessionTemplate.selectList(
                    QmDataConstant.AutoMethod.SELECT_LIST.buildNameSpace(),
                    instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        if (null != mapLis && mapLis.size() != 0) {
            // 如果数据库字段是下划线样式 则进行转换
            Table table = entity.getClass().getAnnotation(Table.class);
            if (table != null && table.style() == Style.UNDERLINE) {
                for (int i = 0; i < mapLis.size(); i++) {
                    mapLis.set(i, QmDataStyleTools.transformMapForHump(mapLis.get(i)));
                }
            }
            list = QmConvertUtil.mapsToBeans(mapLis, clamm);
        }
        LOG.info("autoSelectList elapsed time:{}/ms", System.currentTimeMillis() - time);
        return list;
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, Class<Q> clamm) {
        if (null == entity) {
            return null;
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, false);
        Map<String, Object> map = null;
        try {
            map = sqlSessionTemplate.selectOne(
                    QmDataConstant.AutoMethod.SELECT_ONE.buildNameSpace(),
                    instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        if (map != null) {
            // 如果数据库字段是下划线样式 则进行转换
            Table table = entity.getClass().getAnnotation(Table.class);
            if (table != null && table.style() == Style.UNDERLINE) {
                map = QmDataStyleTools.transformMapForHump(map);
            }
            entity = QmConvertUtil.mapToBean(map, clamm);
        } else {
            entity = null;
        }
        LOG.info("autoSelectOne elapsed time:{}/ms", System.currentTimeMillis() - time);
        return entity;
    }

    @Override
    public <Q> int autoInsert(Q entity) {
        int result = -1;
        if (null == entity) {
            return result;
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, true);
        try {
            result = sqlSessionTemplate.insert(
                    QmDataConstant.AutoMethod.INSERT.buildNameSpace(),
                    instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoInsert elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    @Override
    public <Q> ResultInsert autoInsertGetPrimaryKey(Q entity) {
        int result = -1;
        if (null == entity) {
            return ResultInsert.build(result);
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, true);
        LinkedHashMap<String, Object> paramsMap = instance.getParamsMap();
        try {
            result = sqlSessionTemplate.insert(
                    QmDataConstant.AutoMethod.INSERT_GET_PK.buildNameSpace(), paramsMap);
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoInsert elapsed time:{}/ms", System.currentTimeMillis() - time);
        return ResultInsert.build(result, paramsMap.get(QmDataConstant.INSERT_PRIMARY_MAP_KEY));
    }

    @Override
    public <Q> int autoUpdate(Q entity) {
        int result = -1;
        if (null == entity) {
            return result;
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, true);
        try {
            result = sqlSessionTemplate.update(
                    QmDataConstant.AutoMethod.UPDATE.buildNameSpace(),
                    instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoUpdate elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }


    @Override
    public <Q> int autoDelete(Q entity) {
        int result = -1;
        if (null == entity) {
            return result;
        }
        final long time = System.currentTimeMillis();
        try {
            result = sqlSessionTemplate.delete(
                    QmDataConstant.AutoMethod.DELETE.buildNameSpace(),
                    QmDataModel.getInstance(entity, true).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoDelete elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    @Override
    public <Q> int autoSelectCount(Q entity) {
        int result = -1;
        final long time = System.currentTimeMillis();
        try {
            result = sqlSessionTemplate.selectOne(
                    QmDataConstant.AutoMethod.SELECT_COUNT.buildNameSpace(),
                    QmDataModel.getInstance(entity, false).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoSelectCount elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 配置错误信息
     *
     * @param methodName
     * @return
     */
    private String getErrMsg() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();//调用的方法名
        return "SQL error using " + methodName + "! 使用 " + methodName + " 发生SQL错误!";
    }

}
