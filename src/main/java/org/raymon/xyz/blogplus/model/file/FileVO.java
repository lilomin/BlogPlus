package org.raymon.xyz.blogplus.model.file;

/**
 * Created by lilm on 18-3-11.
 */
public class FileVO {
	
	private String name;
	private Long size;
	private Long updateTime;
	private boolean dir;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getSize() {
		return size;
	}
	
	public void setSize(Long size) {
		this.size = size;
	}
	
	public Long getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	public boolean isDir() {
		return dir;
	}
	
	public void setDir(boolean dir) {
		this.dir = dir;
	}
}
