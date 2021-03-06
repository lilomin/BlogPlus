package org.raymon.xyz.blogplus.service.impl;

import org.raymon.xyz.blogplus.common.constant.CommonConstant;
import org.raymon.xyz.blogplus.common.exception.BlogPlusException;
import org.raymon.xyz.blogplus.common.exception.ExceptionEnum;
import org.raymon.xyz.blogplus.common.utils.MarkDownUtils;
import org.raymon.xyz.blogplus.common.utils.UUIDUtils;
import org.raymon.xyz.blogplus.dao.BlogNavDao;
import org.raymon.xyz.blogplus.dao.BlogTagDao;
import org.raymon.xyz.blogplus.dao.ManagerDao;
import org.raymon.xyz.blogplus.dao.UserDao;
import org.raymon.xyz.blogplus.model.Page;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.BlogNav;
import org.raymon.xyz.blogplus.model.manager.BlogTag;
import org.raymon.xyz.blogplus.model.manager.CalendarCate;
import org.raymon.xyz.blogplus.model.manager.TagChangeParam;
import org.raymon.xyz.blogplus.model.manager.TagCount;
import org.raymon.xyz.blogplus.model.user.User;
import org.raymon.xyz.blogplus.service.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.thymeleaf.util.DateUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by lilm on 18-3-15.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
	
	@Resource
	private ManagerDao managerDao;
	@Resource
	private BlogTagDao blogTagDao;
	@Resource
	private BlogNavDao blogNavDao;
	@Resource
	private UserDao userDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveBlog(Blog blog) {
		int flag;
		String userId = blog.getUserId();
		String blogId = blog.getBlogId();
		if (blogId != null && !blogId.trim().isEmpty()) {
			Blog db = getByBlogId(userId, blogId);
			if (db == null) {
				throw new BlogPlusException(ExceptionEnum.DATA_NOT_FOUND);
			}
			blog.setUpdateTime(new Date());
			blog.setContent(Base64Utils.encodeToString(blog.getContent().getBytes()));
			if (blog.getImage() == null || blog.getImage().isEmpty()) {
				blog.setImage(db.getImage());
			}
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
		}
		if (flag > 0 && blog.getTags() != null && blog.getTags().size() > 0 ) {
			blogTagChange(new TagChangeParam(userId, blogId, blog.getTags()));
		}
		return flag > 0;
	}
	
	@Override
	public Page<Blog> getBlogList(String userId, int currentPage, int pageSize, boolean includeHidden, String filter, String tag) {
		if (tag != null && tag.trim().length() > 0) {
			return selectByTag(userId, currentPage, pageSize, includeHidden, tag);
		} else {
			return selectByFilter(userId, currentPage, pageSize, includeHidden, filter);
		}
	}
	
	private Page<Blog> selectByFilter(String userId, int currentPage, int pageSize, boolean includeHidden, String filter) {
		String filterMonth = null;
		String filterYear = null;
		if (filter != null && filter.contains("-")) {
			filterMonth = filter;
		} else {
			filterYear = filter;
		}
		int total = managerDao.countAll(userId, includeHidden, filterMonth, filterYear);
		int offset = currentPage * pageSize - pageSize;
		if (offset > total) {
			return new Page<>();
		}
		List<Blog> dataList = managerDao.selectByPage(userId, pageSize, offset, includeHidden, filterMonth, filterYear, null);
		
		setterBlogTags(userId, dataList);
		Page<Blog> page = new Page<>(total, currentPage, pageSize, dataList);
		return page;
	}
	
	private Page<Blog> selectByTag(String userId, int currentPage, int pageSize, boolean includeHidden, String tag) {
		int total = managerDao.countAllByTag(userId, tag);
		int offset = currentPage * pageSize - pageSize;
		if (offset > total) {
			return new Page<>();
		}
		List<Blog> dataList = managerDao.selectByPage(userId, pageSize, offset, includeHidden, null, null, tag);
		setterBlogTags(userId, dataList);
		Page<Blog> page = new Page<>(total, currentPage, pageSize, dataList);
		return page;
	}
	
	private void setterBlogTags(String userId, List<Blog> dataList) {
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
				b.setTags(generateBlogTags(blogTagDao.getTags(userId, b.getBlogId())));
			}
		}
	}
	
	/**
	 * 生成博客对应的访问路径
	 * @param blog
	 * @return
	 */
	private String generateBlogPath(Blog blog) {
		if (blog != null) {
			return blog.getBlogId();
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
	
	private List<String> generateBlogTags(List<BlogTag> blogTags) {
		if (blogTags == null || blogTags.size() <= 0) {
			return new ArrayList<>();
		}
		
		return blogTags.stream().map(BlogTag::getTag).collect(Collectors.toList());
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
			blog.setTags(generateBlogTags(blogTagDao.getTags(userId, blogId)));
			String markdown = new String(Base64Utils.decodeFromString(blog.getContent()));
			String content = MarkDownUtils.parseMarkDown2Html(markdown);
			blog.setContent(content);
		}
		return blog;
	}
	
	@Override
	public Blog getByBlogIdMarkDown(String userId, String blogId) {
		Blog blog = managerDao.selectByBlogId(userId, blogId);
		if (blog != null) {
			blog.setCreateDay(generateCreateDay(blog.getCreateTime()));
			blog.setTags(generateBlogTags(blogTagDao.getTags(userId, blogId)));
			String markdown = new String(Base64Utils.decodeFromString(blog.getContent()));
			blog.setContent(markdown);
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
			blog.setTags(generateBlogTags(blogTagDao.getTags(userId, blog.getBlogId())));
			String markdown = new String(Base64Utils.decodeFromString(blog.getContent()));
			String content = MarkDownUtils.parseMarkDown2Html(markdown);
			blog.setContent(content);
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
	public void blogReadPlus(String userId, String blogId) {
		managerDao.blogReadTimesPlus(userId, blogId);
	}
	
	@Override
	public boolean blogTagChange(TagChangeParam param) {
		if (param == null) {
			return true;
		}
		List<String> tags = param.getTags();
		if (tags == null || tags.isEmpty()) {
			blogTagDao.deleteByBlogId(param.getUserId(), param.getBlogId());
			return true;
		}
		Set<String> tagSet = tags.stream()
				.filter(tag -> tag != null && tag.trim().length() > 0)
				.collect(Collectors.toSet());
		int count = 0;
		blogTagDao.deleteByBlogId(param.getUserId(), param.getBlogId());
		
		for (String tag : tagSet) {
			BlogTag blogTag = new BlogTag(param.getBlogId(), param.getUserId(), tag);
			blogTag.setCreateTime(new Date());
			int flag = blogTagDao.saveBlogTag(blogTag);
			count = count + flag;
		}
		return count > 0;
	}
	
	@Override
	public List<CalendarCate> getBlogCalendarCate(String userId) {
		List<CalendarCate> calendarCates = managerDao.groupByCreateMonth(userId);
		if (calendarCates == null) {
			return new ArrayList<>();
		}
		Map<String, Integer> yearDataMaps = new HashMap<>();
		calendarCates.forEach(calendarCate -> {
			// MM-YYYY
			String date = calendarCate.getFilterValue();
			if (date != null && date.contains("-")) {
				String[] arr = date.split("-");
				if (arr.length == 2) {
					yearDataMaps.merge(arr[1], calendarCate.getCount(), (oldV, newV) -> oldV + calendarCate.getCount());
				}
			}
		});
		
		List<CalendarCate> result = new ArrayList<>();
		yearDataMaps.forEach((k, v) ->
			result.add(new CalendarCate(k + "年写了些啥呢", k, v))
		);
		
		return result.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TagCount> getAllBlogTags(String userId) {
		List<BlogTag> blogTags = blogTagDao.getAllTags(userId);
		Map<String, Integer> tagMap = new HashMap<>();
		for (BlogTag blogTag : blogTags) {
			String tag = blogTag.getTag();
			Integer count = tagMap.get(tag);
			if (count == null) {
				tagMap.put(tag, 1);
			} else {
				tagMap.put(tag, ++count);
			}
		}
		List<TagCount> results = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : tagMap.entrySet()) {
			results.add(new TagCount(entry.getKey(), String.valueOf(entry.getValue())));
		}
		return results.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	@Override
	public List<BlogNav> getAllBlogNav(String userId) {
		return blogNavDao.list(userId);
	}
	
	@Override
	public BlogNav getBlogNavByAlias(String userId, String navAlias) {
		return blogNavDao.selectByNavTitle(userId, navAlias);
	}
}
