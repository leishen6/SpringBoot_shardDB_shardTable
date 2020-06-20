package com.lyl.service;

import com.lyl.bean.Role;

/**
 * @PACKAGE_NAME: com.lyl.service
 * @ClassName: RoleService
 * @Description:
 * @Date: 2020-06-19 13:02
 **/
public interface RoleService {

    /**
     *  新增角色
     * @param role
     */
    boolean addRole(Role role);

    /**
     * 根据角色名字查询角色信息
     * @param roleName
     * @return
     */
    Role findRoleByName(String roleName);
}
