package org.raymon.xyz.blogplus.controller;

import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.CalendarCate;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.FileService;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.raymon.xyz.blogplus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by lilm on 18-3-14.
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	@Resource
	private ManagerService managerService;
	@Resource
	private UserService userService;
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
	 * @return
	 */
	@RequestMapping("/")
	public String toHome(Model model, @RequestParam(value = "filter", required = false) String filter) {
		User userInfo = userService.queryByUserId(CommonConstant.DEFAULT_USER);
		// List<CalendarCate> cateList = managerService.getBlogCalendarCate(CommonConstant.DEFAULT_USER);
		model.addAttribute("user", userInfo);
		// model.addAttribute("cateList", cateList);
		if (filter != null) {
			model.addAttribute("filter", filter);
		}
		return "home";
	}
	
	@RequestMapping("/achieve")
	public String toAchieve(Model model) {
		return "achieve";
	}
	
	@RequestMapping("/about")
	public String toMarkDownPage(@RequestParam(value = "userId", defaultValue = CommonConstant.DEFAULT_USER) String userId,
	                             Model model) {
		Blog result = managerService.getByBlogTitle(userId, CommonConstant.ABOUT_TITLE);
		if (result == null) {
			return toHome(model, null);
		}
		result.setTitle("关于我");
		model.addAttribute("blog", result);
		return "post";
	}
	
	/**
	 * 编写博客页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit_post")
	public String newPost(Model model, @RequestParam(value = "blogId",required = false) String blogId, HttpSession session) {
		User u = (User) session.getAttribute(CommonConstant.SESSION_KEY);
		if (u != null && blogId != null && blogId.length() > 0) {
			String userId = u.getUserId();
			Blog blog = managerService.getByBlogIdMarkDown(userId, blogId);
			model.addAttribute("blog", blog);
		}
		return "edit_post";
	}
	
	@RequestMapping("/management/{page}")
	public String management(Model model, @PathVariable("page") Integer page, HttpSession session) {
		User u = (User) session.getAttribute(CommonConstant.SESSION_KEY);
		if (u == null) {
			throw new BlogPlusException(ExceptionEnum.SERVER_ERROR);
		}
		Page data = managerService.getBlogList(u.getUserId(), page, 10, true, null);
		model.addAttribute("page", data);
		return "management";
	}
	
	/**
	 * 博客展示页面
	 * @param userId
	 * @param title
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/post/{title}", method = RequestMethod.GET)
	public String blogPost(@RequestParam(value = "userId", defaultValue = CommonConstant.DEFAULT_USER) String userId,
	                       @PathVariable("title") String title, Model model, HttpSession session) {
		if (title == null) {
			return "home";
		}
		title = new String(Base64Utils.decodeFromUrlSafeString(title));
		Blog result = managerService.getByBlogTitle(userId, title);
		if (result == null) {
			return toHome(model, null);
		}
		model.addAttribute("blog", result);
		return "post";
	}
	
}
