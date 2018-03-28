package org.raymon.xyz.blogplus.model.manager;

/**
 * Created by lilm on 18-3-15.
 */
public class BlogTag {
	
	private String blogId;
	private String userId;
	private String tag;
	
	public String getBlogId() {
		return blogId;
	}
	
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
}
