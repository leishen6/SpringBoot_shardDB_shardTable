package com.lyl.service.impl;

import com.lyl.bean.User;
import com.lyl.dao.UserDao;
import com.lyl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: UserServiceImpl
 * @Description:
 * @date: 2019年8月23日 下午4:29:01
 */
@Service("userService") // 表示此是服务层
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)// 添加上事务
    public boolean addUser(User user) {
        boolean flag = false;
        try {
            userDao.addUser(user);
            flag = true;
        } catch (Exception e) {
            logger.error("add user fail .", e);
        }
        return flag;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)// 添加上事务
    public boolean updateUser(User user) {
        boolean flag = false;
        try {
            userDao.updateUser(user);
            flag = true;
        } catch (Exception e) {
            logger.error("update user fail .", e);
        }
        return flag;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)// 添加上事务
    public boolean deleteUser(String userName) {
        boolean flag = false;
        try {
            userDao.deleteUser(userName);
            flag = true;
        } catch (Exception e) {
            logger.error("delete user fail .", e);
        }
        return flag;
    }


    @Override
    public User findUserByName(String userName) {
        return userDao.findByName(userName);
    }


    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

}
