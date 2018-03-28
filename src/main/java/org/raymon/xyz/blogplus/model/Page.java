package org.raymon.xyz.blogplus.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lilm on 18-3-15.
 */
public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = -1747089888152689052L;
	private int total;
	private int currentPage;
	private int pageSize;
	private List<T> list;
	
	public Page(int total, int currentPage, int pageSize, List<T> list) {
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.list = list;
	}
	
	public Page() {
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public List<T> getList() {
		return list;
	}
	
	public void setList(List<T> list) {
		this.list = list;
	}
}
