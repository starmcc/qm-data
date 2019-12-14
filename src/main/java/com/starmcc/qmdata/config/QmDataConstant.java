package com.starmcc.qmdata.config;

import com.starmcc.qmdata.exception.QmDataException;

/**
 * @Author: qm
 * @Date: 2019/12/14 14:11
 */
public class QmDataConstant {

    /**
     * 命名空间
     */
    public static final String QM_NAMESPACE = "QmBaseMapper.";
    /**
     * 自动insert时返回自增主键id
     */
    public static final String INSERT_PRIMARY_MAP_KEY = "INSERT_PRIMARY_MAP_KEY";

    /**
     * 自动SQL方法名枚举
     */
    public enum AutoMethod {
        INSERT_GET_PK("insertGetPrimaryKey"),
        INSERT("insert"),
        DELETE("delete"),
        UPDATE("update"),
        SELECT_ONE("selectOne"),
        SELECT_LIST("selectList"),
        SELECT_COUNT("selectCount");

        private String id;

        private AutoMethod(String id){
            this.id = id;
        }

        public String getId() {
            return id;
        }

        /**
         * 构建MapperNameSpace
         * @return
         */
        public String buildNameSpace(){
            return QmDataConstant.QM_NAMESPACE + this.id;
        }
    }
}
