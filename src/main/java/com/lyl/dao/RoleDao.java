package com.lyl.dao;

import com.lyl.bean.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @PACKAGE_NAME: com.lyl.dao
 * @ClassName: RoleDao
 * @Description:
 * @Date: 2020-06-19 12:58
 **/
@Mapper
public interface RoleDao {

    void addRole(Role role);

    Role findRoleByName(String roleName);
}
