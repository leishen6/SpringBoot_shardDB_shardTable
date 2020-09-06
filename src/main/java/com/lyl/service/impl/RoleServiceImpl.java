package com.lyl.service.impl;

import com.lyl.bean.Role;
import com.lyl.dao.RoleDao;
import com.lyl.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @PACKAGE_NAME: com.lyl.service.impl
 * @ClassName: RoleServiceImpl
 * @Description:
 * @Date: 2020-06-19 13:04
 **/
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean addRole(Role role) {
        boolean flag = false;
        try {
            roleDao.addRole(role);
            flag = true;
        } catch (Exception e) {
            logger.error("add role fail .", e);
        }
        return flag;
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleDao.findRoleByName(roleName);
    }
}
