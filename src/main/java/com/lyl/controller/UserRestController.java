package com.lyl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.bean.User;
import com.lyl.constant.SystemConstant;
import com.lyl.service.UserService;
import com.lyl.utils.RedisTemplateUtils;
import com.lyl.utils.RequestParameter;
import com.lyl.utils.Response;
import com.lyl.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: UserRestController
 * @Description:  在Controller中没有必要根据删除、修改等特意的去使用deleteMapping、putMapping等，
 *               因为这些都是需要有请求参数的，所以可以直接只使用postMapping即可
 * @date: 2019年8月23日 下午4:30:56
 */

@RestController
@Api(tags = "用户数据接口")
@RequestMapping(value = "/v1/api")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * redis中key时用户前缀
	 */
	private String prex = "User_";
	
	@Autowired
	RedisTemplateUtils redisU;

	@Autowired
	private UserService userService;


	/**
	 * @ApiOperation这个注解是使用swagger生成rest接口的在线文档
	 * @param requestParameter
	 * @return
	 */
	@ApiOperation(value="新增用户", notes="新增用户")
	@PostMapping("/user/addUser")
	public Response<Boolean> addUser(@RequestBody RequestParameter<User> requestParameter) {
		logger.info("request: "+ requestParameter.toString());
		logger.info("开始新增...");

		boolean flag = userService.addUser(requestParameter.getData());
		Response response = null;
		if (flag) {
			response = new Response(flag, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());

			// 新增用户成功，所以将可能之前缓存的findAll全部查询结果删除
			logger.info("清空redis中key："+ SystemConstant.USER_FINDUSERALL +"缓存数据...");
			redisU.deleteByKey(SystemConstant.USER_FINDUSERALL);
		}
		else {
			response = new Response(flag, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}
    
	
	@ApiOperation(value="根据name更新用户", notes="根据名字更新用户数据")
	@PostMapping("/user/updateUserByName")
	public Response<Boolean> updateUserByName(@RequestBody RequestParameter<User> requestParameter) {
		logger.info("request: "+ requestParameter.toString());
		logger.info("开始更新...");

		boolean flag = userService.updateUser(requestParameter.getData());
		Response response = null;
		if (flag) {
			response = new Response(flag, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());

			// 更新成功，需要将redis中与此 User 相关的缓存数据清除
			String name = requestParameter.getData().getName();
			logger.info("清空redis中与 "+name+" 相关的缓存数据...");
			redisU.deleteByKey(SystemConstant.USER_FINDUSERALL);
			redisU.deleteByKey(prex + name);
		}
		else {
			response = new Response(flag, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}

	
	@ApiOperation(value="根据用户name删除用户", notes="根据名字删除用户数据")
	@PostMapping("/user/deleteUserByName")
	public Response<Boolean> delete(@RequestBody RequestParameter<String> requestParameter) {
		logger.info("request: "+ requestParameter.toString());
		logger.info("开始删除... " + requestParameter.getData());

		boolean flag = userService.deleteUser(requestParameter.getData());
		Response response = null;
		if (flag) {
			response = new Response(flag, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());

			// 删除成功，需要将redis中与此 User 相关的缓存数据清空
			String name = requestParameter.getData();
			logger.info("清空redis中与 "+name+" 相关的缓存数据...");
			redisU.deleteByKey(SystemConstant.USER_FINDUSERALL);
			redisU.deleteByKey(prex + name);
		}
		else {
			response = new Response(flag, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}
	
	

	@ApiOperation(value="根据name查询用户", notes="根据名字查询用户数据")
	@PostMapping("/user/findUserByName")
	public Response<User> findUserByName(@RequestBody RequestParameter<String> requestParameter) {
		logger.info("request: "+ requestParameter.toString());
		logger.info("开始根据名字查询数据...");

		User user = redisU.get("User_"+requestParameter.getData(), User.class);
		
		if (user == null) {
			user = userService.findUserByName(requestParameter.getData());
		}
		
		Response response = null;
		if (user != null) {
			response = new Response(user, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());

			logger.info("将数据放到redis缓存中......");
			redisU.set(prex + requestParameter.getData(), user);
		}
		else {
			response = new Response("", ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}
	
	
	
	@ApiOperation(value="查询全部用户", notes="查询全部用户数据")
	@GetMapping("/user/findUserAll")
	public Response<List<User>> findAll() {
		logger.info("开始查询所有数据...");

		List<User> findAll;
		//redis缓存中查询
		findAll = redisU.get(SystemConstant.USER_FINDUSERALL, List.class);
		
		if (!(findAll != null && findAll.size()>0)) {
			findAll = userService.findAll();
		}
		
		Response response = null;
		if (findAll!=null && findAll.size() != 0) {
			response = new Response(findAll, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());

			logger.info("将数据放到redis缓存中......");
			redisU.set(SystemConstant.USER_FINDUSERALL, findAll);
		}
		else {
			response = new Response(findAll, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}
	
	
	
	
	@ApiOperation(value="分页查询全部用户", notes="分页查询全部用户数据")
	@PostMapping("/user/findUserAllByPage")
	public Response<PageInfo> findAllByPage(@RequestBody RequestParameter<User> requestParameter) {
		logger.info("request: "+ requestParameter.toString());
		logger.info("开始分页查询数据...");
		
		PageHelper.startPage(requestParameter.getData().getPageinfo().getPageNum(), requestParameter.getData().getPageinfo().getPageSize());
		List<User> findAll = userService.findAll();
		PageInfo<User> pageinfo = new PageInfo<>(findAll);
		
		Response response = null;
		if (findAll.size() != 0) {
			response = new Response(pageinfo, ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message());
		}
		else {
			response = new Response(pageinfo, ResponseCode.ERROR.code(), ResponseCode.ERROR.message());
		}
		logger.info("response: "+response.toString() +"\n");
		return response;
	}

}