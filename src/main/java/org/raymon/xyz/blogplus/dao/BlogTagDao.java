package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.raymon.xyz.blogplus.model.manager.BlogTag;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lilm on 18-3-29.
 */
@Component
public interface BlogTagDao {
	
	@Insert(
			"insert into blog_tag values " +
					"(#{blogId}, #{userId}, #{tag}, #{createTime})"
	)
	int saveBlogTag(BlogTag tag);
	
	@Select(
			"select * from blog_tag where user_id = #{userId} and blog_id = #{blogId}"
	)
	@Results({
			@Result(property = "blogId", column = "blog_id"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "tag", column = "tag"),
			@Result(property = "createTime", column = "create_time")
	})
	List<BlogTag> getTags(@Param("userId") String userId, @Param("blogId") String blogId);
	
	@Delete(
			"delete from blog_tag where user_id = #{userId} and blog_id = #{blogId}"
	)
	int deleteByBlogId(@Param("userId") String userId, @Param("blogId") String blogId);
	
}
