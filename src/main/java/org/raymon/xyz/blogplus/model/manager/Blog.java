package org.raymon.xyz.blogplus.model.manager;

import java.util.Date;
import java.util.List;

/**
 * Created by lilm on 18-3-15.
 */
public class Blog {
	
	private String blogId;
	private String userId;
	private String title;
	private String content;
	private String description;
	private Date createTime;
	private Date updateTime;
	private boolean hidden;
	private String image;
	private Integer readTimes;
	
	// 访问路径
	private String path;
	// 作者名称
	private String author;
	// 创建时间字符串 YYYY-MM-DD
	private String createDay;
	
	private List<String> tags;
	
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public Integer getReadTimes() {
		return readTimes;
	}
	
	public void setReadTimes(Integer readTimes) {
		this.readTimes = readTimes;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCreateDay() {
		return createDay;
	}
	
	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}
	
	@Override
	public String toString() {
		return "Blog{" +
				"blogId='" + blogId + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
