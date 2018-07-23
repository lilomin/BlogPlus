package org.raymon.xyz.blogplus.model.manager;

/**
 * Created by lilm on 18-7-23.
 */
public class BlogNav {
	
	private Integer navId;
	private String blogId;
	private String userId;
	private String navTitle;
	private String navAlias;
	
	public Integer getNavId() {
		return navId;
	}
	
	public void setNavId(Integer navId) {
		this.navId = navId;
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
	
	public String getNavTitle() {
		return navTitle;
	}
	
	public void setNavTitle(String navTitle) {
		this.navTitle = navTitle;
	}
	
	public String getNavAlias() {
		return navAlias;
	}
	
	public void setNavAlias(String navAlias) {
		this.navAlias = navAlias;
	}
}