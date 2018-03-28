package org.raymon.xyz.blogplus.service;

import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;

/**
 * Created by lilm on 18-3-15.
 */
public interface ManagerService {
	
	String PATH_CONNECTOR = "/";
	
	boolean saveBlog(Blog blog);
	
	Page<Blog> getBlogList(String userId, int currentPage, int pageSize);
	
	Blog getByBlogId(String userId, String blogId);
	
	Blog getByBlogTitle(String userId, String title);
	
}
