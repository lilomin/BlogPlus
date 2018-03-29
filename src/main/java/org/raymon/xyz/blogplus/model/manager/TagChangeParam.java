package org.raymon.xyz.blogplus.model.manager;

import java.util.List;

/**
 * Created by lilm on 18-3-29.
 */
public class TagChangeParam {
	
	private String userId;
	private String blogId;
	private List<String> tags;
	
	public TagChangeParam() {
	}
	
	public TagChangeParam(String userId, String blogId, List<String> tags) {
		this.userId = userId;
		this.blogId = blogId;
		this.tags = tags;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getBlogId() {
		return blogId;
	}
	
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
