package com.starmcc.qmdata.base;

import com.starmcc.qmdata.exception.QmDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author qm
 * @Date 2019/12/29 13:18
 * @Description 兼容 1.x 版本（后续版本不再维护）
 */
@Deprecated
public abstract class AbstractQmDataOld extends AbstractQmDataAutoReload {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractQmDataOld.class);

    /**
     * 命名空间
     */
    private static final String QM_NAMESPACE = "QmBaseMapper.";


    /**
     * 解析命名空间
     *
     * @param sqlName
     * @return
     */
    private String getSqlName(String sqlName) {
        try {
            String methodName = sqlName.substring(sqlName.indexOf("Mapper") + 6);
            String nameSpace = sqlName.substring(0, sqlName.indexOf("Mapper") + 6);
            return nameSpace + "." + methodName;
        } catch (Exception e) {
            throw new QmDataException("Mapper命名空间错误!'" + sqlName + "' Error！", e);
        }
    }


    @Deprecated
    @Override
    public <M> List<M> selectList(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        try {
            List<M> list = sqlSessionTemplate.selectList(this.getSqlName(sqlName), params);
            LOG.info("selectList elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return list;
        } catch (Exception e) {
            throw new QmDataException(super.getErrMsg(), e);
        }
    }

    @Deprecated
    @Override
    public <M> M selectOne(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        try {
            M obj = sqlSessionTemplate.selectOne(this.getSqlName(sqlName), params);
            LOG.info("selectOne elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return obj;
        } catch (Exception e) {
            throw new QmDataException(super.getErrMsg(), e);
        }
    }

    @Deprecated
    @Override
    public int insert(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        try {
            int result = sqlSessionTemplate.insert(this.getSqlName(sqlName), params);
            LOG.info("insert elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(super.getErrMsg(), e);
        }
    }

    @Deprecated
    @Override
    public int update(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        try {
            int result = sqlSessionTemplate.update(this.getSqlName(sqlName), params);
            LOG.info("update elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(super.getErrMsg(), e);
        }
    }

    @Deprecated
    @Override
    public int delete(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        try {
            int result = sqlSessionTemplate.delete(this.getSqlName(sqlName), params);
            LOG.info("delete elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(super.getErrMsg(), e);
        }
    }


}
