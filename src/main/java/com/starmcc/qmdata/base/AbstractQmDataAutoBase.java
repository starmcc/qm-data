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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: qm
 * @Date: 2019/12/29 14:11
 */
public abstract class AbstractQmDataAutoBase implements QmData {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractQmDataAutoReload.class);

    /**
     * 获取Mybatis SqlSession
     */
    protected SqlSessionTemplate sqlSessionTemplate;

    // =======================================华丽的分割线===========================================

    @Override
    public <Q> List<Q> autoSelectList(Q entity, String where, String orderBy, Class<Q> clamm) {
        final long time = System.currentTimeMillis();
        if (Objects.isNull(entity) && Objects.isNull(clamm)) {
            throw new QmDataException("selectList method param is not found");
        }
        // 判断实体类是否为空，为空则自动创建新的对象。
        entity = this.buildBean(entity, clamm);
        clamm = Objects.isNull(entity) ? clamm : (Class<Q>) entity.getClass();

        QmDataModel<Q> instance = QmDataModel.getInstance(entity, where, orderBy, false);
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
    public <Q> Q autoSelectOne(Q entity, String where, String orderBy, Class<Q> clamm) {
        final long time = System.currentTimeMillis();
        if (Objects.isNull(entity) && Objects.isNull(clamm)) {
            throw new QmDataException("selectList method param is not found");
        }
        // 判断实体类是否为空，为空则自动创建新的对象。
        entity = this.buildBean(entity, clamm);
        clamm = Objects.isNull(entity) ? clamm : (Class<Q>) entity.getClass();

        QmDataModel<Q> instance = QmDataModel.getInstance(entity, where, orderBy, false);
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
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoInsert method param is not found");
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
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoInsertGetPrimaryKey method param is not found");
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, true);
        Map<String, Object> paramsMap = instance.getParamsMap();
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
    public <Q> int autoUpdate(Q entity, String where) {
        int result = -1;
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoUpdate method param is not found");
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, where, true);
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
    public <Q> int autoDelete(Q entity, String where) {
        int result = -1;
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoDelete method param is not found");
        }
        final long time = System.currentTimeMillis();
        try {
            result = sqlSessionTemplate.delete(
                    QmDataConstant.AutoMethod.DELETE.buildNameSpace(),
                    QmDataModel.getInstance(entity, where, true).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoDelete elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    @Override
    public <Q> Long autoSelectCount(Q entity, String where) {
        final long time = System.currentTimeMillis();
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoSelectCount method param is not found");
        }
        Long result = 0L;
        try {
            result = sqlSessionTemplate.selectOne(
                    QmDataConstant.AutoMethod.SELECT_COUNT.buildNameSpace(),
                    QmDataModel.getInstance(entity, where, false).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoSelectCount elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 判断实体类是否为空，为空则自动创建新的对象。
     *
     * @param bean
     * @param clamm
     * @return
     */
    public <T> T buildBean(T bean, Class<T> clamm) {
        try {
            return Objects.nonNull(bean) ? bean : clamm.newInstance();
        } catch (Exception e) {
            throw new QmDataException("传递实体为空，且自动创建失败!");
        }
    }

    /**
     * 配置错误信息
     *
     * @param methodName
     * @return
     */
    public String getErrMsg() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();//调用的方法名
        return "SQL error using " + methodName + "! 使用 " + methodName + " 发生SQL错误!";
    }

}
