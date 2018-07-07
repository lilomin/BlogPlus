package org.raymon.xyz.blogplus.model.manager;

/**
 * Created by lilm on 18-7-7.
 */
public class TagCount {
	
	private String tag;
	private String count;
	
	public TagCount() {
	}
	
	public TagCount(String tag, String count) {
		this.tag = tag;
		this.count = count;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getCount() {
		return count;
	}
	
	public void setCount(String count) {
		this.count = count;
	}
}
