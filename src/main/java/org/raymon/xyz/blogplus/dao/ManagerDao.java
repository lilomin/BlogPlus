package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.raymon.xyz.blogplus.model.manager.Blog;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by lilm on 18-3-15.
 */
@Component
public interface ManagerDao {
	
	@Insert(
			"insert into blog values " +
					"(#{blogId}, #{userId}, #{title}, #{content}, #{createTime}, #{updateTime}, #{description}, #{hidden}, #{image})"
	)
	int createBlog(Blog blog);
	
	@Update(
			"update table blog set title = #{title}, content = #{content}, description = #{description}, update_time = #{updateTime}" +
					"where blog_id = #{blogId} and user_id = #{userId}"
	)
	int updateBlog(Blog blog);
	
	@Select(
			"select blog_id, user_id, title, content, description, hidden, image, create_time, update_time from blog where user_id = #{userId} and hidden = 0 " +
					"order by create_time limit #{limit} offset #{offset}"
	)
	@Results({
			@Result(property = "blogId", column = "blog_id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "title", column = "title"),
			@Result(property = "description", column = "description"),
			@Result(property = "hidden", column = "hidden"),
			@Result(property = "image", column = "image"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time")
	})
	List<Blog> selectByPage(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	
	@Select(
			"select count(1) from blog where user_id = #{userId}"
	)
	int countAll(String userId);
	
	@Select(
			"select blog_id, user_id, title, content, description, hidden, image, create_time, update_time from blog " +
					"where user_id = #{userId} and blog_id = #{blogId}"
	)
	@Results({
			@Result(property = "blogId", column = "blog_id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "title", column = "title"),
			@Result(property = "content", column = "content"),
			@Result(property = "description", column = "description"),
			@Result(property = "hidden", column = "hidden"),
			@Result(property = "image", column = "image"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time")
	})
	Blog selectByBlogId(@Param("userId") String userId, @Param("blogId") String blogId);
	
	@Select(
			"select blog_id, user_id, title, content, description, hidden, image, create_time, update_time from blog " +
					"where user_id = #{userId} and title = #{title}"
	)
	@Results({
			@Result(property = "blogId", column = "blog_id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "title", column = "title"),
			@Result(property = "content", column = "content"),
			@Result(property = "description", column = "description"),
			@Result(property = "hidden", column = "hidden"),
			@Result(property = "image", column = "image"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time")
	})
	Blog selectByBlogTitle(@Param("userId") String userId, @Param("title")String title);
	
	@Update(
			"update table blog set hidden = #{hidden}, update_time = #{updateTime}" +
					"where blog_id = #{blogId} and user_id = #{userId}"
	)
	int updateBlogStatus(@Param("userId") String userId, @Param("blogId") String blogId,
	                     @Param("hidden") boolean hidden, @Param("updateTime") Date updateTime);
	
}
