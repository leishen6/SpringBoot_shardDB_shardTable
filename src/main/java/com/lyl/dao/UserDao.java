package com.lyl.dao;

import com.lyl.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *@Title: UserDao 
 * @Description: 
 * @date: 2019年8月23日 下午4:25:40
 */
@Mapper
public interface UserDao {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(String userName);

    User findByName(String userName);

    List<User> findAll();

}
