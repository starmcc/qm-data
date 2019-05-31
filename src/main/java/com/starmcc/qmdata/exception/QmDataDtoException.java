package com.starmcc.qmdata.exception;

/**
 * @author qm
 * @date 2019/1/13 3:31
 * @Description QmDataDto异常
 */
public class QmDataDtoException extends RuntimeException {

    /**
     * 提供无参数的构造方法
     */
    public QmDataDtoException() {
    }


    /**
     * 提供一个有参数的构造方法，可自动生成
     * @param message
     */
    public QmDataDtoException(String message) {
        // 把参数传递给Throwable的带String参数的构造方法
        super(message);
    }

    public QmDataDtoException(String message, Throwable cause) {
        super(message, cause);
    }
}
