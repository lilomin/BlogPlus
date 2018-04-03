package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.model.user.LoginParam;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.security.WebSecurityConfig;
import org.raymon.xyz.blogplus.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lilm on 18-3-11.
 */
@RestController
@RequestMapping("api/v1/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@PostMapping("register")
	public Result register(@RequestBody User user) {
		Boolean flag = userService.createUser(user);
		if (flag) {
			return ResultUtils.success(user);
		} else {
			return ResultUtils.fail("register failed!");
		}
	}
	
	@GetMapping
	public Result getUser(
			@RequestParam(value = "userId", required = false) String userId,
	        @RequestParam(value = "username", required = false) String username) {
		User user = null;
		if (!StringUtils.isEmpty(userId)) {
			user = userService.queryByUserId(userId);
		} else {
			user = userService.queryByUsername(username);
		}
		
		if (user == null) {
			return ResultUtils.fail("user not found!");
		}
		return ResultUtils.success(user);
	}
	
	@PostMapping("/login")
	public Result userLogin(@RequestBody LoginParam loginParam, HttpSession session) {
		String username = loginParam.getUsername();
		String password = loginParam.getPassword();
		
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return ResultUtils.fail("params is empty!");
		}
		
		User u = userService.userCheck(username, password);
		if (u == null) {
			return ResultUtils.fail("incorrect password!");
		}
		// 设置session
		session.setAttribute(CommonConstant.SESSION_KEY, u);
		return ResultUtils.success(u);
	}
	
}
