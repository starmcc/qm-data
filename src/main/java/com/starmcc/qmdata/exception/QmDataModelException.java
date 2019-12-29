package com.starmcc.qmdata.exception;

/**
 * @author qm
 * @date 2019/1/13 3:31
 * @Description QmDataModelException异常
 */
public class QmDataModelException extends RuntimeException {

    private static final long serialVersionUID = 9150297096635823583L;

    /**
     * 提供无参数的构造方法
     */
    public QmDataModelException() {
    }


    /**
     * 提供一个有参数的构造方法，可自动生成
     * @param message
     */
    public QmDataModelException(String message) {
        // 把参数传递给Throwable的带String参数的构造方法
        super(message);
    }

    public QmDataModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
