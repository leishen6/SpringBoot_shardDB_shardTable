package com.lyl.dao;

import com.lyl.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: UserDao
 * @Description: 用户 dao
 * @date: 2019年8月23日 下午4:25:40
 */
@Mapper
public interface UserDao {

    /**
     * 添加用户
     *
     * @param user
     */
    void addUser(User user);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据用户名 删除用户
     *
     * @param userName
     */
    void deleteUser(String userName);

    /**
     * @param userName
     * @return
     */
    User findByName(String userName);

    /**
     * 查询全部用户
     *
     * @return
     */
    List<User> findAll();

}
