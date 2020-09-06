package com.lyl.dao;

import com.lyl.bean.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @PACKAGE_NAME: com.lyl.dao
 * @ClassName: RoleDao
 * @Description: 角色 dao
 * @Date: 2020-06-19 12:58
 **/
@Mapper
public interface RoleDao {

    /**
     * 添加角色
     *
     * @param role
     */
    void addRole(Role role);

    /**
     * 根据名称 查询角色
     *
     * @param roleName
     * @return
     */
    Role findRoleByName(String roleName);
}
