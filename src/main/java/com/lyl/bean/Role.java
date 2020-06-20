package com.lyl.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @PACKAGE_NAME: com.lyl.bean
 * @ClassName: Role
 * @Description:  角色
 * @Date: 2020-06-19 12:54
 **/
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 编号 */
    private int id;
    /** 姓名 */
    private String roleName;


    public Role(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
