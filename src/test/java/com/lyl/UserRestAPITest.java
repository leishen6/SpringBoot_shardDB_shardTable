package com.lyl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lyl.bean.User;
import com.lyl.utils.RequestParameter;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * @Title: UserRestAPITest
 * @Description: user 单元测试
 * @date: 2019年8月23日 下午8:27:34
 */
public class UserRestAPITest {

    private String userName = "单例模式";


    @Test
    public void addUser() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/addUser";

        // 请求参数
        RequestParameter<User> re = new RequestParameter<>();
        User user = new User();
        user.setAge(25);
        user.setName(userName);
        re.setData(user);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }

    @Test
    public void deleteUserByName() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/deleteUserByName";
        // 请求参数
        RequestParameter<String> re = new RequestParameter<>();
        re.setData(userName);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }

    @Test
    public void updateUser() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/updateUserByName";

        // 请求参数
        RequestParameter<User> re = new RequestParameter<>();
        User user = new User();
        user.setAge(36);
        user.setName(userName);
        re.setData(user);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }


    @Test
    public void findUserByName() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/findUserByName";

        // 请求参数
        RequestParameter<String> re = new RequestParameter<>();
        re.setData(userName);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }


    @Test
    public void findAll() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/findUserAll";

        // 无参数的查询使用 getForObject()
        String result = restTemplate.getForObject(url, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }


    @Test
    public void findAllByPage() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/v1/api/user/findUserAllByPage";

        // 请求参数
        RequestParameter<User> re = new RequestParameter<>();
        // 分页工具类
        PageInfo pageinfo = new PageInfo();
        User user = new User(pageinfo);
        user.getPageinfo().setPageNum(1);
        user.getPageinfo().setPageSize(10);
        re.setData(user);

        String result = restTemplate.postForObject(url, re, String.class);

        JSONObject resultJsonObject = JSON.parseObject(result);
        System.out.println(resultJsonObject.getString("date"));
    }

}
