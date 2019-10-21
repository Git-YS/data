package com.hrms.common;

public class Response {
    private static final int OK = 0;
    private static final int ERROR = -1;
    private int code;
    private String msg;
    private Object data;

    public static Response ok() {
        Response response = new Response();
        response.code = OK;
        return response;
    }
    public static Response ok(Object data) {
        Response response = new Response();
        response.code = OK;
        response.data = data;
        return response;
    }

    public static Response error() {
        Response response = new Response();
        response.code = ERROR;
        return response;
    }

    public static Response error(Object data) {
        Response response = new Response();
        response.code = ERROR;
        response.data = data;
        return response;
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
}
