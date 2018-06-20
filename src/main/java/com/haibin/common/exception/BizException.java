package com.haibin.common.exception;

/**
 * 业务异常类，继承自RuntimeException
 *
 * @author haibin.tang
 * @create 2018-05-25 下午7:38
 **/
public class BizException extends RuntimeException{

    public BizException(String message) {
        super(message);
    }
}
