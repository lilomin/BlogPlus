package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.raymon.xyz.blogplus.model.manager.CalendarCate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lilm on 18-3-15.
 */
@Component
public interface ManagerDao {
	
	@Insert(
			"insert into blog values " +
					"(#{blogId}, #{userId}, #{title}, #{content}, #{createTime}, #{updateTime}, #{description}, #{hidden}, #{image}, 0)"
	)
	int createBlog(Blog blog);
	
	@Update(
			"update blog set title = #{title}, content = #{content}, description = #{description}, update_time = #{updateTime}, image = #{image} " +
					" where blog_id = #{blogId} and user_id = #{userId}"
	)
	int updateBlog(Blog blog);
	
	@SelectProvider(type = BlogProvider.class, method = "getBlogListByPage")
	@Results(id = "blogMap", value = {
			@Result(property = "blogId", column = "blog_id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "title", column = "title"),
			@Result(property = "description", column = "description"),
			@Result(property = "hidden", column = "hidden"),
			@Result(property = "image", column = "image"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time"),
			@Result(property = "readTimes", column = "read_times")
	})
	List<Blog> selectByPage(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset,
	                        @Param("includeHidden") boolean includeHidden, @Param("createMonth") String createMonth);
	
	@SelectProvider(type = BlogProvider.class, method = "getCountByFilter")
	int countAll(@Param("userId") String userId, @Param("includeHidden") boolean includeHidden, @Param("createMonth") String createMonth);
	
	@Select(
			"select blog_id, user_id, title, content, description, hidden, image, create_time, update_time, read_times from blog " +
					"where user_id = #{userId} and blog_id = #{blogId}"
	)
	@ResultMap("blogMap")
	Blog selectByBlogId(@Param("userId") String userId, @Param("blogId") String blogId);
	
	@Select(
			"select blog_id, user_id, title, content, description, hidden, image, create_time, update_time, read_times from blog " +
					"where user_id = #{userId} and title = #{title}"
	)
	@ResultMap("blogMap")
	Blog selectByBlogTitle(@Param("userId") String userId, @Param("title")String title);
	
	@Update(
			"update blog set hidden = #{hidden}, update_time = #{updateTime} " +
					"where blog_id = #{blogId} and user_id = #{userId}"
	)
	int updateBlogStatus(@Param("userId") String userId, @Param("blogId") String blogId,
	                     @Param("hidden") boolean hidden, @Param("updateTime") Date updateTime);
	
	@Update(
			"update blog set read_times = read_times + 1 " +
					"where blog_id = #{blogId} and user_id = #{userId}"
	)
	int blogReadTimesPlus(@Param("userId") String userId, @Param("blogId") String blogId);
	
	@Select(
			"select count(1) as blog_count, create_month from (select blog_id, DATE_FORMAT(create_time, '%m-%Y') as create_month from blog " +
					"where user_id = #{userId} and hidden = 0 ) as temp group by create_month order by create_month desc"
	)
	@Results({
			@Result(property = "filterValue", column = "create_month"),
			@Result(property = "count", column = "blog_count")
	})
	List<CalendarCate> groupByCreateMonth(@Param("userId") String userId);
	
	
	class BlogProvider {
		
		public String getBlogListByPage(Map<String, Object> params) {
			StringBuilder sql =
					new StringBuilder("select blog_id, user_id, title, content, description, hidden, image, create_time, update_time, read_times from blog where ");
			
			generate(params, sql);
			Object limitParam = params.get("limit");
			Object offsetParam = params.get("offset");
			
			if (offsetParam == null) {
				offsetParam = 1;
			}
			if (limitParam == null) {
				limitParam = 6;
			}
			sql.append(" order by create_time desc limit ").append(limitParam).append(" offset ").append(offsetParam);
			
			return sql.toString();
		}
		
		public String getCountByFilter(Map<String, Object> params) {
			StringBuilder sql = new StringBuilder("select count(1) from blog where ");
			generate(params, sql);
			
			return sql.toString();
		}
		
		private void generate(Map<String, Object> params, StringBuilder sql) {
			Object hiddenParam = params.get("includeHidden");
			Object createMonthParam = params.get("createMonth");
			
			sql.append("user_id = '").append(params.get("userId")).append("'");
			if (hiddenParam != null && !(Boolean) hiddenParam) {
				// 不包含隐藏博客
				sql.append(" and hidden = false");
			}
			
			if (createMonthParam != null && !createMonthParam.toString().trim().isEmpty()) {
				// MM-YYYY
				sql.append(" and DATE_FORMAT(create_time, '%m-%Y') = '").append(createMonthParam).append("'");
			}
		}
		
	}
	
}
