package org.raymon.xyz.blogplus.model.manager;

import java.util.Date;

/**
 * Created by lilm on 18-3-15.
 */
public class BlogTag {
	
	private String blogId;
	private String userId;
	private String tag;
	private Date createTime;
	
	public BlogTag() {
	}
	
	public BlogTag(String blogId, String userId, String tag) {
		this.blogId = blogId;
		this.userId = userId;
		this.tag = tag;
	}
	
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
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
