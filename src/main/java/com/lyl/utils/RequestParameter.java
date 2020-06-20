package com.lyl.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.UUID;


/**
 * 请求报文
 *
 * Created By Ray on 2019/08/23
 */
public class RequestParameter<T> {

    /**
     * 请求时间戳
     */
    private long timestamp;

    /**
     * 版本号
     */
    private String version;
    
    
    private String serialNo;
    
    
	/**
     * 业务参数,不同业务接口不同.(请求参数)
     */
    private T data;
    
    
    public RequestParameter() {
		this.timestamp = new Date().getTime();
		this.serialNo = UUID.randomUUID().toString().replaceAll("-", "");
	}

    
    
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
