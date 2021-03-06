package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.BlogNav;
import org.raymon.xyz.blogplus.model.manager.CalendarCate;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;
import org.raymon.xyz.blogplus.model.manager.TagCount;

import java.util.List;
import java.util.Map;

/**
 * Created by lilm on 18-3-15.
 */
public interface ManagerService {
	
	/**
	 * 保存博客
	 *
	 * @param blog
	 * @return
	 */
	boolean saveBlog(Blog blog);
	
	/**
	 * 分页获取博客内容
	 *
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<Blog> getBlogList(String userId, int currentPage, int pageSize, boolean includeHidden, String filter, String tag);
	
	/**
	 * 根据博客id查询
	 *
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return
	 */
	Blog getByBlogId(String userId, String blogId);
	
	/**
	 * 根据博客id查询
	 *
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return
	 */
	Blog getByBlogIdMarkDown(String userId, String blogId);
	
	/**
	 * 根据博客标题查询
	 *
	 * @param userId
	 * @param title
	 * @return
	 */
	Blog getByBlogTitle(String userId, String title);
	
	/**
	 * 博客是否隐藏开关
	 *
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return 操作结果
	 */
	boolean blogSwitch(String userId, String blogId);
	
	/**
	 * 博客阅读次数加1
	 *
	 * @param userId
	 * @param blogId
	 */
	void blogReadPlus(String userId, String blogId);
	
	/**
	 * 覆盖博客标签
	 *
	 * @param param 操作参数
	 * @return 操作结果
	 */
	boolean blogTagChange(TagChangeParam param);
	
	/**
	 * 获取博客日志组
	 *
	 * @param userId
	 * @return
	 */
	List<CalendarCate> getBlogCalendarCate(String userId);
	
	/**
	 * 所有标签
	 * @param userId
	 * @return
	 */
	List<TagCount> getAllBlogTags(String userId);
	
	/**
	 * 获取当前用户所有导航栏设置
	 * @param userId
	 * @return
	 */
	List<BlogNav> getAllBlogNav(String userId);
	
	/**
	 * 根据navTitle搜索
	 * @param navTitle
	 * @return
	 */
	BlogNav getBlogNavByAlias(String userId, String navTitle);
}
