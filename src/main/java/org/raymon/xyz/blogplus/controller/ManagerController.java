package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.result.Result;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
	public Result getBlogByPage(@RequestParam("userId") String userId,
	                            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
	                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Page page = managerService.getBlogList(userId, currentPage, pageSize);
		return ResultUtils.success(page);
	}
	
	@RequestMapping(value = "/switch", method = RequestMethod.GET)
	@ResponseBody
	public Result getBlogByPage(@RequestParam("userId") String userId, @RequestParam("blogId") String blogId) {
		boolean flag = managerService.blogSwitch(userId, blogId);
		return ResultUtils.success(flag);
	}
	
	@RequestMapping(value = "/tag/change", method = RequestMethod.GET)
	@ResponseBody
	public Result changeBlogTag(@RequestBody TagChangeParam param) {
		boolean flag = managerService.blogTagChange(param);
		return ResultUtils.success(flag);
	}
	
}
