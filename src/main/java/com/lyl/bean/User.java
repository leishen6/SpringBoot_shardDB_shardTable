package com.lyl.bean;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;

/**
 * @Title: User
 * @Description: 用户
 * @date: 2019年8月23日 下午4:21:11
 */
@SuppressWarnings("rawtypes")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 分页插件所需要使用的
     */
    private PageInfo pageinfo;

    public User() {

    }

    public User(PageInfo pageinfo) {
        this.pageinfo = pageinfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PageInfo getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageInfo pageinfo) {
        this.pageinfo = pageinfo;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
