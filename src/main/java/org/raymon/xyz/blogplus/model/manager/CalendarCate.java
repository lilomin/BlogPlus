package org.raymon.xyz.blogplus.model.manager;

/**
 * Created by lilm on 18-5-5.
 */
public class CalendarCate implements Comparable<CalendarCate> {
	
	private String title;
	private String filterValue;
	private Integer count;
	
	public CalendarCate() {
	}
	
	public CalendarCate(String title, String filterValue, Integer count) {
		this.title = title;
		this.filterValue = filterValue;
		this.count = count;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFilterValue() {
		return filterValue;
	}
	
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public int compareTo(CalendarCate o) {
		if (o == null) {
			return 0;
		}
		return this.title.compareTo(o.title) * -1;
	}
	
}
