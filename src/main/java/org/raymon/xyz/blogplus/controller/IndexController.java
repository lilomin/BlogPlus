package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.utils.MarkDownUtils;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by lilm on 18-3-14.
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	private static final String DEFAULT_USER = "1";
	
	@Resource
	private ManagerService managerService;
	
	@RequestMapping("/home")
	public String toHome(@RequestParam(value = "userId", defaultValue = DEFAULT_USER) String userId, Model model) {
		Page<Blog> result = managerService.getBlogList(userId, 1, 10);
		model.addAttribute("list", result.getList());
		model.addAttribute("currentPage", result.getCurrentPage());
		model.addAttribute("totalPage", result.getTotal() / result.getPageSize() + 1);
		return "home";
	}
	
	@RequestMapping("/edit_post")
	public String newPost(Model model) {
		return "edit_post";
	}
	
	@RequestMapping(value = "/post/{year}/{month}/{day}/{title}", method = RequestMethod.GET)
	public String blogPost(@RequestParam(value = "userId", defaultValue = DEFAULT_USER) String userId,
	                       @PathVariable("title") String title, Model model) {
		Blog result = managerService.getByBlogTitle(userId, title);
		if (result == null) {
			return toHome(userId, model);
		}
		model.addAttribute("blog", result);
		return "post";
	}
	
}
