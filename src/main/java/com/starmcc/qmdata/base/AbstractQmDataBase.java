package com.starmcc.qmdata.base;

import com.starmcc.qmdata.common.QmData;
import com.starmcc.qmdata.exception.QmDataException;
import com.starmcc.qmdata.note.Style;
import com.starmcc.qmdata.note.Table;
import com.starmcc.qmdata.util.QmConvertUtil;
import com.starmcc.qmdata.util.QmDataStyleTools;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author qm
 * @Title QmBase实现类
 * @Date 注释时间：2019/1/23 12:43
 */
public abstract class AbstractQmDataBase implements QmData {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractQmDataBase.class);

    /**
     * 命名空间
     */
    private static final String QM_NAMESPACE = "QmBaseMapper.";
    /**
     * 获取Mybatis SqlSession
     */
    protected SqlSessionFactory sqlSessionFactory;

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

    @Override
    public <M> List<M> selectList(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            List<M> list = session.selectList(getSqlName(sqlName), params);
            session.commit();
            LOG.debug("selectList elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return list;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("selectList"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <M> M selectOne(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            M obj = session.selectOne(getSqlName(sqlName), params);
            session.commit();
            LOG.debug("selectOne elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return obj;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("selectOne"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int insert(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.insert(getSqlName(sqlName), params);
            session.commit();
            LOG.debug("insert elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("insert"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int update(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.update(getSqlName(sqlName), params);
            session.commit();
            LOG.debug("update elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("update"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public int delete(String sqlName, Object params) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.delete(getSqlName(sqlName), params);
            session.commit();
            LOG.debug("delete elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("delete"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> List<Q> autoSelectList(Q entity, Class<Q> clamm) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        List<Map> mapLis;
        try {
            if (entity == null) {
                entity = clamm.newInstance();
            }
            mapLis = session.selectList(QM_NAMESPACE + "selectAuto",
                    new QmDataDto<Q>(entity, false).getParamsMap());
            session.commit();
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoSelectList"), e);
        } finally {
            session.close();
        }
        // 如果是空的直接返回null
        if (mapLis == null) {
            LOG.debug("autoSelectList elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return null;
        }
        // 如果数据库字段是下划线样式 则进行转换
        Table table = entity.getClass().getAnnotation(Table.class);
        if (table != null && table.style() == Style.UNDERLINE) {
            for (int i = 0; i < mapLis.size(); i++) {
                Map<String, Object> map = mapLis.get(i);
                map = QmDataStyleTools.transformMapForHump(map);
                mapLis.set(i, map);
            }
        }
        List<Q> list = QmConvertUtil.mapsToBeans(mapLis, clamm);
        LOG.debug("autoSelectList elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
        return list;
    }

    @Override
    public <Q> Q autoSelectOne(Q entity, Class<Q> clamm) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        Map<String, Object> map;
        try {
            if (entity == null) {
                entity = clamm.newInstance();
            }
            map = session.selectOne(QM_NAMESPACE + "selectAutoOne",
                    new QmDataDto<Q>(entity, false).getParamsMap());
            session.commit();
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoSelectOne"), e);
        } finally {
            session.close();
        }
        // 如果是空的直接返回null
        if (map == null) {
            LOG.debug("autoSelectOne elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return null;
        }
        // 如果数据库字段是下划线样式 则进行转换
        Table table = entity.getClass().getAnnotation(Table.class);
        if (table != null && table.style() == Style.UNDERLINE) {
            map = QmDataStyleTools.transformMapForHump(map);
        }
        entity = QmConvertUtil.mapToBean(map, clamm);
        LOG.debug("autoSelectOne elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
        return entity;
    }

    @Override
    public <Q> int autoInsert(Q entity) {
        long time = System.currentTimeMillis();
        if (entity == null) {
            return -1;
        }
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.insert(QM_NAMESPACE + "insertAuto",
                    new QmDataDto<Q>(entity, true).getParamsMap());
            session.commit();
            LOG.debug("autoInsert elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoInsert"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> int autoUpdate(Q entity) {
        long time = System.currentTimeMillis();
        if (entity == null) {
            return -1;
        }
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.update(QM_NAMESPACE + "updateAuto",
                    new QmDataDto<Q>(entity, true).getParamsMap());
            session.commit();
            LOG.debug("autoUpdate elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoUpdate"), e);
        } finally {
            session.close();
        }
    }


    @Override
    public <Q> int autoDelete(Q entity) {
        long time = System.currentTimeMillis();
        if (entity == null) {
            return -1;
        }
        SqlSession session = sqlSessionFactory.openSession();
        try {
            int result = session.delete(QM_NAMESPACE + "deleteAuto",
                    new QmDataDto<Q>(entity, true).getParamsMap());
            session.commit();
            LOG.debug("autoDelete elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoDelete"), e);
        } finally {
            session.close();
        }
    }

    @Override
    public <Q> int autoSelectCount(Q entity, Class<Q> clamm) {
        long time = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            if (entity == null) {
                entity = clamm.newInstance();
            }
            int result = session.selectOne(QM_NAMESPACE + "selectCount",
                    new QmDataDto<Q>(entity, false).getParamsMap());
            session.commit();
            LOG.debug("autoSelectCount elapsed time:" + (System.currentTimeMillis() - time) + "/ms");
            return result;
        } catch (Exception e) {
            throw new QmDataException(getErrorMsg("autoSelectCount"), e);
        } finally {
            session.close();
        }
    }

    /**
     * 配置错误信息
     *
     * @param methodName
     * @return
     */
    private String getErrorMsg(String methodName) {
        return "SQL error using " + methodName + "! 使用 " + methodName + " 发生SQL错误!";
    }

}
