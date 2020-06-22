package com.lyl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyl.bean.Role;
import com.lyl.utils.RequestParameter;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * @PACKAGE_NAME: com.lyl
 * @ClassName: RoleRestAPITest
 * @Description: 单元测试
 * @Date: 2020-06-19 13:24
 **/
public class RoleRestAPITest {

    private String roleName = "程序员";


    @Test
    public void addRole(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/role/addRole";

        // 请求参数
        RequestParameter<Role> re = new RequestParameter<>();
        Role role = new Role();
        role.setRoleName(roleName);
        re.setData(role);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }


    @Test
    public void findRoleByName(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/role/findRoleByName";

        // 请求参数
        RequestParameter<String> re = new RequestParameter<>();
        re.setData(roleName);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }
}
