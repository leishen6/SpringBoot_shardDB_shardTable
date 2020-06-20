package com.lyl.service;

import com.lyl.bean.User;

import java.util.List;

/**
 *@Title: UserService 
 * @Description: 
 * @date: 2019年8月23日 下午4:28:01
 */
public interface UserService {
	
	 /**
     * 新增用户
     * @param user
     * @return
     */
    boolean addUser(User user);
    
    /**
     * 修改用户
     * @param user
     * @return
     */
    boolean updateUser(User user);
    
    
    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean deleteUser(String userName);
    
     /**
     * 根据用户名字查询用户信息
     * @param userName
     */
    User findUserByName(String userName);
    
 
    
    /**
     * 查询所有
     * @return
     */
    List<User> findAll();

}
