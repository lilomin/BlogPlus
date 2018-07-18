package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lilm on 18-3-15.
 */
@Controller
@RequestMapping("api/v1/manager")
public class ManagerController {
	
	@Resource
	private ManagerService managerService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Result saveBlog(@RequestBody Blog blog) {
		boolean flag = managerService.saveBlog(blog);
		if (!flag) {
			return ResultUtils.fail("save blog failed");
		} else {
			return ResultUtils.success(blog);
		}
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Result getBlogByPage(@RequestParam(value = "userId", defaultValue = CommonConstant.DEFAULT_USER) String userId,
	                            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
	                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
	                            @RequestParam(value = "filter", required = false) String filter,
	                            @RequestParam(value = "tag", required = false) String tag) {
		Page page = managerService.getBlogList(userId, currentPage, pageSize, false, filter, tag);
		return ResultUtils.success(page);
	}
	
	@RequestMapping(value = "/switch", method = RequestMethod.GET)
	@ResponseBody
	public Result getBlogByPage(@RequestParam("blogId") String blogId, HttpSession session) {
		User u = (User) session.getAttribute(CommonConstant.SESSION_KEY);
		if (u == null) {
			throw new BlogPlusException(ExceptionEnum.SERVER_ERROR);
		}
		boolean flag = managerService.blogSwitch(u.getUserId(), blogId);
		return ResultUtils.success(flag);
	}
	
	@RequestMapping(value = "/tag/change", method = RequestMethod.GET)
	@ResponseBody
	public Result changeBlogTag(@RequestBody TagChangeParam param) {
		boolean flag = managerService.blogTagChange(param);
		return ResultUtils.success(flag);
	}
	
}
