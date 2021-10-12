package com.starmcc.qmdata.model;

import java.io.Serializable;

/**
 * @author starmcc
 * @version 2019/12/14 16:13
 * 主键返回Result
 */
public class ResultInsert implements Serializable {

    private static final long serialVersionUID = 7077169425585814389L;

    private int rowCount;
    private Object primaryVal;

    public static ResultInsert build(int rowCount) {
        return new ResultInsert(rowCount, null);
    }

    public static ResultInsert build(int rowCount, Object primaryVal) {
        return new ResultInsert(rowCount, primaryVal);
    }

    private ResultInsert() {
    }

    private ResultInsert(int rowCount, Object primaryVal) {
        this.rowCount = rowCount;
        this.primaryVal = primaryVal;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public Object getPrimaryVal() {
        return primaryVal;
    }

    public void setPrimaryVal(Object primaryVal) {
        this.primaryVal = primaryVal;
    }

    @Override
    public String toString() {
        return "ResultInsert{" +
                "rowCount=" + rowCount +
                ", primaryVal=" + primaryVal +
                '}';
    }
}
