package com.starmcc.qmdata.exception;

/**
 * @author starmcc
 * @version 2019/1/13 16:06
 * QmBase错误异常
 */
public class QmDataException extends RuntimeException {
    private static final long serialVersionUID = 932624366579279947L;

    /**
     * 提供无参数的构造方法
     */
    public QmDataException() {
    }


    /**
     * 提供一个有参数的构造方法，可自动生成
     *
     * @param message
     */
    public QmDataException(String message) {
        // 把参数传递给Throwable的带String参数的构造方法
        super(message);
    }

    /**
     * 提供一个带参的构造方法，并且输出一些异常信息
     *
     * @param message
     * @param cause
     */
    public QmDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
