package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.raymon.xyz.blogplus.model.user.User;
import org.springframework.stereotype.Component;

/**
 * Created by lilm on 18-3-11.
 */
@Component
public interface UserDao {
	
	@Select("SELECT user_id, username, password, email, nickname, create_time, update_time FROM blog_user WHERE username = #{username}")
	@Results({
			@Result(property = "userId", column = "user_id"),
			@Result(property = "username", column = "username"),
			@Result(property = "password", column = "password"),
			@Result(property = "email", column = "email"),
			@Result(property = "nickname", column = "nickname"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time"),
			@Result(property = "lastLogicTime", column = "last_logic_time")
	})
	User selectByUsername(String username);
	
	@Select("SELECT user_id, username, password, avatar, email, nickname, sign, create_time, update_time FROM blog_user WHERE user_id = #{userId}")
	@Results({
			@Result(property = "userId", column = "user_id"),
			@Result(property = "username", column = "username"),
			@Result(property = "password", column = "password"),
			@Result(property = "avatar", column = "avatar"),
			@Result(property = "email", column = "email"),
			@Result(property = "nickname", column = "nickname"),
			@Result(property = "sign", column = "sign"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "updateTime", column = "update_time"),
			@Result(property = "lastLogicTime", column = "last_logic_time")
	})
	User selectById(@Param("userId") String userId);
	
	@Insert(
			"insert into blog_user values " +
					"(#{userId}, #{username}, #{nickname}, #{avatar}, #{email}, #{sign}, #{password}, #{createTime}, #{updateTime}, #{lastLoginTime})"
	)
	int insert(User user);
	
}
