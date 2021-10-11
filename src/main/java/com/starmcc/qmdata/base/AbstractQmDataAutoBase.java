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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author starmcc
 * @version 2019/12/29 14:11
 * QmData基础实现
 */
public abstract class AbstractQmDataAutoBase implements QmData {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractQmDataAutoReload.class);

    /**
     * 获取Mybatis SqlSession
     */
    protected SqlSessionTemplate sqlSessionTemplate;

    // =======================================华丽的分割线===========================================

    @Override
    public <Q, M> List<M> autoSelectList(Q entity, String where, String orderBy, Class<M> clamm) {
        if (Objects.isNull(clamm)) {
            throw new QmDataException("selectList method param is not found");
        }
        final long time = System.currentTimeMillis();
        // 判断实体类是否为空，为空则自动创建新的对象。
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, where, orderBy, false, clamm);
        List<Map<String, Object>> mapLis = null;
        List<M> list = new ArrayList<>();
        try {
            // 执行SQL
            mapLis = sqlSessionTemplate.selectList(QmDataConstant.AutoMethod.SELECT_LIST.buildNameSpace(), instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        if (!CollectionUtils.isEmpty(mapLis)) {
            // 如果数据库字段是下划线样式 则进行转换
            Table table = clamm.getAnnotation(Table.class);
            for (Map<String, Object> map : mapLis) {
                list.add(this.transitionBean(table, map, clamm));
            }
        }
        LOG.info("autoSelectList elapsed time:{}/ms", System.currentTimeMillis() - time);
        return list;
    }


    @Override
    public <Q, M> M autoSelectOne(Q entity, String where, String orderBy, Class<M> clamm) {
        if (Objects.isNull(entity) && Objects.isNull(clamm)) {
            throw new QmDataException("selectOne method param is not found");
        }
        final long time = System.currentTimeMillis();
        QmDataModel<Q> instance = QmDataModel.getInstance(entity, where, orderBy, false, clamm);
        Map<String, Object> map = null;
        try {
            map = sqlSessionTemplate.selectOne(QmDataConstant.AutoMethod.SELECT_ONE.buildNameSpace(), instance.getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        Table table = entity.getClass().getAnnotation(Table.class);
        M resultEntity = Objects.nonNull(map) ? this.transitionBean(table, map, clamm) : null;
        LOG.info("autoSelectOne elapsed time:{}/ms", System.currentTimeMillis() - time);
        return resultEntity;
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
            result = sqlSessionTemplate.insert(QmDataConstant.AutoMethod.INSERT.buildNameSpace(), instance.getParamsMap());
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
            result = sqlSessionTemplate.insert(QmDataConstant.AutoMethod.INSERT_GET_PK.buildNameSpace(), paramsMap);
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
            result = sqlSessionTemplate.update(QmDataConstant.AutoMethod.UPDATE.buildNameSpace(), instance.getParamsMap());
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
            result = sqlSessionTemplate.delete(QmDataConstant.AutoMethod.DELETE.buildNameSpace(),
                    QmDataModel.getInstance(entity, where, true).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoDelete elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    @Override
    public <Q, M> Long autoSelectCount(Q entity, String where, Class<M> clamm) {
        final long time = System.currentTimeMillis();
        if (Objects.isNull(entity)) {
            throw new QmDataException("autoSelectCount method param is not found");
        }
        Long result = 0L;
        try {
            result = sqlSessionTemplate.selectOne(QmDataConstant.AutoMethod.SELECT_COUNT.buildNameSpace(),
                    QmDataModel.getInstance(entity, where, null, false, clamm).getParamsMap());
        } catch (Exception e) {
            throw new QmDataException(this.getErrMsg(), e);
        }
        LOG.info("autoSelectCount elapsed time:{}/ms", System.currentTimeMillis() - time);
        return result;
    }

    /**
     * 转换Map
     *
     * @param entity
     * @param map
     * @param <Q>
     * @return
     */
    private <M> M transitionBean(Table table, Map<String, Object> map, Class<M> clamm) {
        if (Objects.nonNull(table) && table.style() == Style.HUMP) {
            return QmConvertUtil.mapToBean(map, clamm);
        }
        // 如果数据库字段是下划线样式 则进行转换
        map = QmDataStyleTools.transformMapForHump(map);
        return QmConvertUtil.mapToBean(map, clamm);
    }

    /**
     * 配置错误信息
     *
     * @param methodName
     * @return msg
     */
    private String getErrMsg() {
        //调用的方法名
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return "SQL error using " + methodName + "! 使用 " + methodName + " 发生SQL错误!";
    }

}
