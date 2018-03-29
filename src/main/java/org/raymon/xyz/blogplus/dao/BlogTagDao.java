package org.raymon.xyz.blogplus.dao;

import org.apache.ibatis.annotations.Insert;
import org.raymon.xyz.blogplus.model.manager.BlogTag;
import org.springframework.stereotype.Component;

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
	
}
