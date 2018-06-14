package com.haibin.common.vo;

import java.io.Serializable;

/**
 * 公共返回对象封装
 *
 * @author haibin.tang
 * @create 2018-05-23 下午9:34
 **/
public class JsonResult implements Serializable {

    private int code;

    private String msg;

    private Object data;

    private String ts = System.currentTimeMillis() + "";

    private static final String SUCCESS_MSG = "SUCCESS";

    public JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(Object data) {
        this.code = 200;
        this.msg = SUCCESS_MSG;
        this.data = data;
    }

    public JsonResult() {
        this.code = 200;
        this.msg = SUCCESS_MSG;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
