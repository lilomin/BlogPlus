package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.service.FileService;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lilm on 18-3-14.
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	private static final String DEFAULT_USER = "1";
	
	@Resource
	private ManagerService managerService;
	@Resource
	private FileService fileService;
	
	/**
	 * 文件系统首页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "file/{path}", method = RequestMethod.GET)
	public String index(@PathVariable("path") String path, Model model) {
		List<FileVO> list = fileService.fileList(path);
		model.addAttribute("files", list);
		return "index";
	}
	
	/**
	 * 博客首页
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/home")
	public String toHome(@RequestParam(value = "userId", defaultValue = DEFAULT_USER) String userId, Model model) {
		Page<Blog> result = managerService.getBlogList(userId, 1, 10);
		model.addAttribute("list", result.getList());
		model.addAttribute("currentPage", result.getCurrentPage());
		model.addAttribute("totalPage", result.getTotal() / result.getPageSize() + 1);
		return "home";
	}
	
	/**
	 * 编写博客页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit_post")
	public String newPost(Model model) {
		return "edit_post";
	}
	
	/**
	 * 博客展示页面
	 * @param userId
	 * @param title
	 * @param model
	 * @return
	 */
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
