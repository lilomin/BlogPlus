package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;

import java.util.List;

/**
 * Created by lilm on 18-3-15.
 */
public interface ManagerService {
	
	/**
	 * 保存博客
	 * @param blog
	 * @return
	 */
	boolean saveBlog(Blog blog);
	
	/**
	 * 分页获取博客内容
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<Blog> getBlogList(String userId, int currentPage, int pageSize, boolean includeHidden);
	
	/**
	 * 根据博客id查询
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return
	 */
	Blog getByBlogId(String userId, String blogId);
	
	/**
	 * 根据博客id查询
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return
	 */
	Blog getByBlogIdMarkDown(String userId, String blogId);
	
	/**
	 * 根据博客标题查询
	 * @param userId
	 * @param title
	 * @return
	 */
	Blog getByBlogTitle(String userId, String title);
	
	/**
	 * 博客是否隐藏开关
	 * @param userId 用户id
	 * @param blogId blogId
	 * @return 操作结果
	 */
	boolean blogSwitch(String userId, String blogId);
	
	/**
	 * 覆盖博客标签
	 * @param param 操作参数
	 * @return 操作结果
	 */
	boolean blogTagChange(TagChangeParam param);
	
}
