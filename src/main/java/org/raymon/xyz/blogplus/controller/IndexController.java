package org.raymon.xyz.blogplus.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.result.ResultUtils;
import org.raymon.xyz.blogplus.common.utils.IpUtil;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.file.FileVO;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.BlogNav;
import org.raymon.xyz.blogplus.model.manager.CalendarCate;
import org.raymon.xyz.blogplus.model.manager.TagCount;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.FileService;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.raymon.xyz.blogplus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lilm on 18-3-14.
 */
@Controller
@RequestMapping("/")
public class IndexController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private ManagerService managerService;
	@Resource
	private UserService userService;
	
	/**
	 * 博客首页
	 * @return
	 */
	@RequestMapping("/")
	public String toHome(Model model, @RequestParam(value = "filter", required = false) String filter) {
		return homePage(model, null, filter);
	}
	
	/**
	 * 博客首页
	 * @return
	 */
	@RequestMapping("/page/{page}")
	public String toHomePage(Model model, @PathVariable(value = "page", required = false) String page,
	                     @RequestParam(value = "filter", required = false) String filter) {
		return homePage(model, page, filter);
	}
	
	private String homePage(Model model, String page, String filter) {
		User userInfo = userService.queryByUserId(CommonConstant.DEFAULT_USER);
		List<CalendarCate> cateList = managerService.getBlogCalendarCate(CommonConstant.DEFAULT_USER);
		List<TagCount> tags = managerService.getAllBlogTags(CommonConstant.DEFAULT_USER);
		
		List emptyList = new ArrayList();
		model.addAttribute("user", userInfo);
		model.addAttribute("cateList", cateList == null ? emptyList : cateList);
		model.addAttribute("tags", tags == null ? emptyList : tags);
		if (filter != null) {
			model.addAttribute("filter", filter);
		}
		
		putNavs(model);
		return "home";
	}
	
	@RequestMapping("/timeline")
	public String toTimeline(Model model) {
		
		putNavs(model);
		return "timeline";
	}
	
	@RequestMapping("/nav/{path}")
	public String toMarkDownPage(@PathVariable("path") String pathTitle, @RequestParam(value = "userId", defaultValue = CommonConstant.DEFAULT_USER) String userId,
	                             Model model) {
		BlogNav nav = managerService.getBlogNavByAlias(userId, pathTitle);
		if (nav == null) {
			return homePage(model, null, null);
		}
		Blog result = managerService.getByBlogId(userId, nav.getBlogId());
		if (result == null) {
			return homePage(model, null, null);
		}
		
		result.setTitle(nav.getNavTitle());
		model.addAttribute("blog", result);
		
		putNavs(model);
		return "post";
	}
	
	/**
	 * 编写博客页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/management/edit_post")
	public String newPost(Model model, @RequestParam(value = "blogId",required = false) String blogId, HttpSession session) {
		User u = (User) session.getAttribute(CommonConstant.SESSION_KEY);
		if (u != null && blogId != null && blogId.length() > 0) {
			String userId = u.getUserId();
			Blog blog = managerService.getByBlogIdMarkDown(userId, blogId);
			model.addAttribute("blog", blog);
		}
		
		putNavs(model);
		return "edit_post";
	}
	
	@RequestMapping("/management/{page}")
	public String management(Model model, @PathVariable("page") Integer page, HttpSession session) {
		User u = (User) session.getAttribute(CommonConstant.SESSION_KEY);
		if (u == null) {
			throw new BlogPlusException(ExceptionEnum.SERVER_ERROR);
		}
		Page data = managerService.getBlogList(u.getUserId(), page, 10, true, null, null);
		model.addAttribute("page", data);
		
		putNavs(model);
		return "management";
	}
	
	private static LoadingCache<String, Set<String>> readerCache;
	
	private boolean needReadTimesPlus(String blogId, String ip){
		if (readerCache != null) {
			Set<String> ips = null;
			try {
				ips = readerCache.get(blogId);
			} catch (ExecutionException e) {
			    LOGGER.warn("readerCache.get error:", e);
			}
			LOGGER.info("readerCache ip:{} cache:{}", ip, readerCache.asMap());
			if (ips == null || ips.contains(ip)) {
				return false;
			}
			ips.add(ip);
			readerCache.put(blogId, ips);
			return true;
		} else {
			readerCache = CacheBuilder.newBuilder()
					.expireAfterWrite(8, TimeUnit.HOURS)
					.initialCapacity(50).maximumSize(5000)
					.build(new CacheLoader<String, Set<String>>() {
						@Override
						public Set<String> load(String s) throws Exception {
							Set<String> ips = new HashSet<>();
							ips.add(ip);
							return ips;
						}
					});
		}
		return true;
	}
	
	/**
	 * 博客展示页面
	 * @param userId
	 * @param blogId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/post/{blogId}", method = RequestMethod.GET)
	public String blogPost(@RequestParam(value = "userId", defaultValue = CommonConstant.DEFAULT_USER) String userId,
	                       @PathVariable("blogId") String blogId, Model model, HttpSession session, HttpServletRequest request) {
		if (blogId == null) {
			return "home";
		}
		Blog result = managerService.getByBlogId(userId, blogId);
		if (result == null) {
			return homePage(model, null, null);
		}
		if (needReadTimesPlus(blogId, IpUtil.getIpAddr(request))) {
			managerService.blogReadPlus(userId, blogId);
		}
		model.addAttribute("blog", result);
		
		putNavs(model);
		return "post";
	}
	
	private void putNavs(Model model) {
		List<BlogNav> navs = managerService.getAllBlogNav(CommonConstant.DEFAULT_USER);
		model.addAttribute("navs", navs == null ? new ArrayList() : navs);
	}
	
}
