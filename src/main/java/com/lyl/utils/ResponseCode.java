package com.lyl.utils;

/**
 *@Title: ResponseCode 
 * @Description:    使用枚举类封装好的响应状态码及对应的响应消息
 * @date: 2019年8月23日 下午7:12:50
 */
public enum ResponseCode {
	
	SUCCESS(1200,"请求成功"),
	
	ERROR(1400,"请求失败");
	
	
	private Integer code;
	
	private String message;
	
	private ResponseCode(Integer code,String message){
		this.code = code;
		this.message = message;
	}
	
	public Integer code(){
		return this.code;
	}
	
	public String message(){
		return this.message;
	}

}
