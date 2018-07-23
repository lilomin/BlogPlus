package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.raymon.xyz.blogplus.model.manager.BlogNav;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lilm on 18-7-23.
 */
@Component
public interface BlogNavDao {
	
	@Select(
			"select nav_id, blog_id, user_id, nav_title, nav_alias from blog_nav where user_id = #{userId}"
	)
	@Results({
			@Result(column = "nav_id", property = "navId"),
			@Result(column = "blog_id", property = "blogId"),
			@Result(column = "user_id", property = "userId"),
			@Result(column = "nav_title", property = "navTitle"),
			@Result(column = "nav_alias", property = "navAlias")
	})
	List<BlogNav> list(@Param("userId") String userId);
	
	@Select(
			"select nav_id, blog_id, user_id, nav_title, nav_alias from blog_nav where user_id = #{userId} and nav_alias = #{navAlias}"
	)
	@Results({
			@Result(column = "nav_id", property = "navId"),
			@Result(column = "blog_id", property = "blogId"),
			@Result(column = "user_id", property = "userId"),
			@Result(column = "nav_title", property = "navTitle"),
			@Result(column = "nav_alias", property = "navAlias")
	})
	BlogNav selectByNavTitle(@Param("userId") String userId, @Param("navAlias") String navAlias);
	
}
