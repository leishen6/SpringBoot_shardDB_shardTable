package com.lyl.controller;

import com.lyl.bean.Role;
import com.lyl.service.RoleService;
import com.lyl.utils.RequestParameter;
import com.lyl.utils.Response;
import com.lyl.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PACKAGE_NAME: com.lyl.controller
 * @ClassName: RoleRestController
 * @Description:
 * @Date: 2020-06-19 13:10
 **/
@RestController
@Api(tags = "角色数据接口")
@RequestMapping(value = "/v1/api")
public class RoleRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleService roleService;


    @ApiOperation(value="新增角色", notes="新增角色")
    @PostMapping("/role/addRole")
    public Response<Boolean> addRole(@RequestBody RequestParameter<Role> requestParameter) {
        logger.info("request: "+ requestParameter.toString());
        logger.info("开始新增...");

        boolean flag = roleService.addRole(requestParameter.getData());
        Response response = null;
        if (flag) {
            response = new Response(flag, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());
        }
        else {
            response = new Response(flag, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
        }
        logger.info("response: "+response.toString() +"\n");
        return response;
    }



    @ApiOperation(value="根据name查询角色", notes="根据名字查询角色数据")
    @PostMapping("/role/findRoleByName")
    public Response<Role> findRoleByName(@RequestBody RequestParameter<String> requestParameter) {
        logger.info("request: "+ requestParameter.toString());
        logger.info("开始根据名字查询角色数据...");

        Role role =  roleService.findRoleByName(requestParameter.getData());

        Response response = null;
        if (role != null) {
            response = new Response(role, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());
        }
        else {
            response = new Response("", ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
        }
        logger.info("response: "+response.toString() +"\n");
        return response;
    }

}
