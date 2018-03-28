package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.utils.UUIDUtils;
import org.raymon.xyz.blogplus.dao.ManagerDao;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.thymeleaf.util.DateUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lilm on 18-3-15.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
	
	@Resource
	private ManagerDao managerDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveBlog(Blog blog) {
		int flag;
		String userId = blog.getUserId();
		String blogId = blog.getBlogId();
		if (blogId != null) {
			Blog db = getByBlogId(userId, blogId);
			if (db == null) {
				throw new BlogPlusException(ExceptionEnum.DATA_NOT_FOUND);
			}
			blog.setUpdateTime(new Date());
			flag = managerDao.updateBlog(blog);
		} else {
			Blog db = getByBlogTitle(blog.getUserId(), blog.getTitle());
			if (db != null) {
				throw new BlogPlusException(ExceptionEnum.DATA_EXISTS);
			}
			blog.setContent(Base64Utils.encodeToString(blog.getContent().getBytes()));
			blog.setCreateTime(new Date());
			blog.setUpdateTime(new Date());
			blog.setBlogId(UUIDUtils.createUUID());
			flag = managerDao.createBlog(blog);
			if (flag > 0) {
				// todo saveTags
			}
		}
		return flag > 0;
	}
	
	@Override
	public Page<Blog> getBlogList(String userId, int currentPage, int pageSize) {
		int total = managerDao.countAll(userId);
		int offset = currentPage * pageSize - pageSize;
		List<Blog> dataList = managerDao.selectByPage(userId, pageSize, offset);
		if (dataList != null && !dataList.isEmpty()) {
			Iterator<Blog> it = dataList.iterator();
			while (it.hasNext()) {
				Blog b = it.next();
				String path = generateBlogPath(b);
				if (path == null || path.isEmpty()) {
					it.remove();
				} else {
					b.setPath(path);
				}
			}
		}
		return new Page<>(total, currentPage, pageSize, dataList);
	}
	
	private String generateBlogPath(Blog blog) {
		if (blog != null) {
			StringBuilder sb = new StringBuilder();
			Date createTime = blog.getCreateTime();
			Integer year = DateUtils.year(createTime);
			Integer month = DateUtils.month(createTime);
			Integer day = DateUtils.day(createTime);
			
			return sb.append(year).append(PATH_CONNECTOR)
					.append(month).append(PATH_CONNECTOR)
					.append(day).append(PATH_CONNECTOR).append(blog.getTitle()).toString();
		}
		return null;
	}
	
	@Override
	public Blog getByBlogId(String userId, String blogId) {
		Blog blog = managerDao.selectByBlogId(userId, blogId);
		blog.setContent(new String(Base64Utils.decodeFromString(blog.getContent())));
		return blog;
	}
	
	@Override
	public Blog getByBlogTitle(String userId, String title) {
		Blog blog = managerDao.selectByBlogTitle(userId, title);
		if (blog != null) {
			blog.setContent(new String(Base64Utils.decodeFromString(blog.getContent())));
		}
		return blog;
	}
}
