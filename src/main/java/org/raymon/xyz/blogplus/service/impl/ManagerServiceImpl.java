package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.utils.UUIDUtils;
import org.raymon.xyz.blogplus.dao.BlogTagDao;
import org.raymon.xyz.blogplus.dao.ManagerDao;
import org.raymon.xyz.blogplus.dao.UserDao;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.BlogTag;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.thymeleaf.util.DateUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by lilm on 18-3-15.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
	
	private static final String PATH_CONNECTOR = "/";
	
	@Resource
	private ManagerDao managerDao;
	@Resource
	private BlogTagDao blogTagDao;
	@Resource
	private UserDao userDao;
	
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
			blogId = UUIDUtils.createUUID();
			blog.setBlogId(blogId);
			flag = managerDao.createBlog(blog);
			if (flag > 0 && blog.getTags() != null && blog.getTags().size() > 0 ) {
				blogTagChange(new TagChangeParam(userId, blogId, blog.getTags()));
			}
		}
		return flag > 0;
	}
	
	@Override
	public Page<Blog> getBlogList(String userId, int currentPage, int pageSize) {
		int total = managerDao.countAll(userId);
		int offset = currentPage * pageSize - pageSize;
		if (offset > total) {
			return new Page<>();
		}
		List<Blog> dataList = managerDao.selectByPage(userId, pageSize, offset);
		if (dataList != null && !dataList.isEmpty()) {
			Iterator<Blog> it = dataList.iterator();
			while (it.hasNext()) {
				Blog b = it.next();
				b.setCreateDay(generateCreateDay(b.getCreateTime()));
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
	
	/**
	 * 生成博客对应的访问路径
	 * @param blog
	 * @return
	 */
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
	
	private String generateCreateDay(Date createTime) {
		String day = "";
		if (createTime != null) {
			day = DateUtils.format(createTime, "yyyy-MM-dd", Locale.CHINA);
		}
		return day;
	}
	
	@Override
	public Blog getByBlogId(String userId, String blogId) {
		Blog blog = managerDao.selectByBlogId(userId, blogId);
		if (blog != null) {
			blog.setCreateDay(generateCreateDay(blog.getCreateTime()));
			User user = userDao.selectById(userId);
			if (user != null) {
				blog.setAuthor(user.getNickname());
			}
			blog.setContent(new String(Base64Utils.decodeFromString(blog.getContent())));
		}
		return blog;
	}
	
	@Override
	public Blog getByBlogTitle(String userId, String title) {
		Blog blog = managerDao.selectByBlogTitle(userId, title);
		if (blog != null) {
			blog.setCreateDay(generateCreateDay(blog.getCreateTime()));
			User user = userDao.selectById(userId);
			if (user != null) {
				blog.setAuthor(user.getNickname());
			}
			blog.setContent(new String(Base64Utils.decodeFromString(blog.getContent())));
		}
		return blog;
	}
	
	@Override
	public boolean blogSwitch(String userId, String blogId) {
		Blog blog = managerDao.selectByBlogId(userId, blogId);
		if (blog == null) {
			throw new BlogPlusException(ExceptionEnum.DATA_NOT_FOUND);
		}
		boolean hidden = blog.isHidden();
		int flag = managerDao.updateBlogStatus(userId, blogId, !hidden, new Date());
		return flag > 0;
	}
	
	@Override
	public boolean blogTagChange(TagChangeParam param) {
		if (param == null) {
			return true;
		}
		List<String> tags = param.getTags();
		if (tags == null || tags.isEmpty()) {
			return true;
		}
		int count = 0;
		for (String tag : new HashSet<>(tags)) {
			BlogTag blogTag = new BlogTag(param.getBlogId(), param.getUserId(), tag);
			blogTag.setCreateTime(new Date());
			int flag = blogTagDao.saveBlogTag(blogTag);
			count = count + flag;
		}
		return count > 0;
	}
}
