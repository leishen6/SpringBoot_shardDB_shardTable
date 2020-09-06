package com.lyl.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @Title: Response
 * @Description: 封装的统一的响应返回类
 * @date: 2019年8月23日 下午7:07:13
 */
@SuppressWarnings("serial")
public class Response<T> implements Serializable {

    /**
     * 响应数据
     */
    private T date;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应描述信息
     */
    private String message;

    public Response(T date, Integer code, String message) {
        super();
        this.date = date;
        this.code = code;
        this.message = message;
    }


    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
